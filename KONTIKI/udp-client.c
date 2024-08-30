#include "contiki.h"
#include "net/routing/routing.h"
#include "random.h"
#include "net/netstack.h"
#include "net/ipv6/simple-udp.h"

#include "sys/log.h"
#define LOG_MODULE "App"
#define LOG_LEVEL LOG_LEVEL_INFO

#define WITH_SERVER_REPLY  1
#define UDP_CLIENT_PORT	8765
#define UDP_SERVER_PORT	5678

static struct simple_udp_connection udp_conn;

#define MAX_READINGS 10
#define SEND_INTERVAL (60 * CLOCK_SECOND)
#define FAKE_TEMPS 5

static struct simple_udp_connection udp_conn;
static unsigned batchedTemperatures[MAX_READINGS];

/*---------------------------------------------------------------------------*/
PROCESS(udp_client_process, "UDP client");
AUTOSTART_PROCESSES(&udp_client_process);
/*---------------------------------------------------------------------------*/
static unsigned
get_temperature()
{
  static unsigned fake_temps [FAKE_TEMPS] = {30, 25, 20, 15, 10};
  return fake_temps[random_rand() % FAKE_TEMPS];
  
}
/*---------------------------------------------------------------------------*/
static void
udp_rx_callback(struct simple_udp_connection *c,
         const uip_ipaddr_t *sender_addr,
         uint16_t sender_port,
         const uip_ipaddr_t *receiver_addr,
         uint16_t receiver_port,
         const uint8_t *data,
         uint16_t datalen)
{
  // not used
}
/*---------------------------------------------------------------------------*/
PROCESS_THREAD(udp_client_process, ev, data)
{
  static struct etimer periodic_timer;
  uip_ipaddr_t dest_ipaddr;//dest of packet
  static unsigned temperature;
  static int isBatching = 0;
  static uint8_t i = 0;

  PROCESS_BEGIN();

  /* Initialize UDP connection */
  simple_udp_register(&udp_conn, UDP_CLIENT_PORT, NULL,
                      UDP_SERVER_PORT, udp_rx_callback);

  /* Initialize temperature buffer */
  for (i=0; i<MAX_READINGS; i++) {
    batchedTemperatures[i] = 0;
  }  

  
  //set periodic timer
  etimer_set(&periodic_timer, random_rand() % SEND_INTERVAL);
  while(1) {
    static int next = 0;
    PROCESS_WAIT_EVENT_UNTIL(etimer_expired(&periodic_timer));

    //checks whether the node is attached to RPL tree
    if(NETSTACK_ROUTING.node_is_reachable() && NETSTACK_ROUTING.get_root_ipaddr(&dest_ipaddr)) { //server is reachable
      if(!isBatching){ //send current temperature
        temperature = get_temperature();//take current temperature
        /* Send to DAG root */
        LOG_INFO("Sending temperature %u to ", temperature);
        LOG_INFO_6ADDR(&dest_ipaddr);
        LOG_INFO_("\n");
      
      }else{//send average temperature
        unsigned sum = 0;
        unsigned no = 0;  

        //compute local average using buffer of temperatures
        for (i = 0; i < MAX_READINGS; i++) {
          if (batchedTemperatures[i]!=0){
            sum = sum+batchedTemperatures[i];
            no++;
          }
        }
        temperature = (unsigned) ((float)sum)/no; //cast to unsigned the local average
        LOG_INFO("Local average is %u. Stopped batching.\n", temperature);
        next = 0;       //reset batching
        isBatching = 0; //reset batching mode
  
        //reset temperatures
        for (i=0; i<MAX_READINGS; i++) {
          batchedTemperatures[i] = 0;
        }  
      }

      simple_udp_sendto(&udp_conn, &temperature, sizeof(temperature), &dest_ipaddr);
    } else {//server is not reachable
      LOG_INFO("Not reachable yet. Batching.\n");
      isBatching = 1;

      batchedTemperatures[next++] = get_temperature();
      if (next == MAX_READINGS) {
        next = 0;
      }

    }

    /* Add some jitter to desynchronize transmission coming from different nodes */
    etimer_set(&periodic_timer, SEND_INTERVAL
      - CLOCK_SECOND + (random_rand() % (2 * CLOCK_SECOND)));
  }

  PROCESS_END();
}
/*---------------------------------------------------------------------------*/

