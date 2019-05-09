# SmartBin
## Smart Waste Collection in Smart City

### Project Description

The Project aims to optimise the route taken by garbage truck if we know aproiri which bins are full in a cost effective manner. The bins are equipped with IR sensors to detect the level of trash. The Communicates the status of the bin (full/empty) to a server using public Wifi. The Android App on the truck drivers app allows him to find an optimal path covering ony the full bins using Travelling Salesman Problem and Google Maps. 

### The Android Application, Google Maps and Travelling Salesman Problem	

The Android Appication uses the data from free public server to check the status of the waste bins. Based on the set position of the waste bins the optimal Path is suggested to the driver of Garbage Truck. 
The distances between bins and time taken to travel between them is calculated using Google Maps. As google maps has a upper limit of only 25 stopovers (bins between origin and destination ), we implemented a customised Travelling Salesman Problem to solve for optimal path.

### The Internet of Things: SmartBin Hardware

The hardware consists of IR sensos with  Wi-Fi module ESP8266 Nodemcu version 12E. The Sensors tells us if the bin is full and the Wifi module updates the status on the server. The value can be then read from server by the Android.
