Android - SpeedTest.net Data Visualizer
=======================================

[Speedtest.net][applink] android application data visualization using Google Maps and other means (in works).


This is a very simple mapping app, which shows points where speed test was done and associated information with it.

Why This?
==========
Though Speedtest.net android app records location information with the test data, there is currently no way to visualize the data in map. This is an effort to fill this gap, and possibly implement other ways to generate chart to understand even more on data stored by the app.  


How to use
=========== 
This app depends on exported data from [Speedtest.net][applink]. Currently there are 2 ways to input data into this visualizer app:
 1. By launching Speedtest.net app, and use existing export feature.
 2. Or, use already exported data - just copy data and paste into input box 

TODO: Add video URL

Credits
=======
 * Apache Commons Lang3 - Utility classes for Java - [http://commons.apache.org/proper/commons-lang/]
 * Apache Commons CSV - parser used to parse CSV data from SpeedTest.net app - [http://commons.apache.org/proper/commons-csv/]
 * Google Maps V2 - [https://developers.google.com/maps/documentation/android/]

Privacy
========
This app does *NOT* collect any personal identifiable information.
App does not store data used to visualize exported data. Once app exits, data is removed from memory.


[applink]: https://play.google.com/store/apps/details?id=org.zwanoo.android.speedtest "Speedtest.net Android App at Google Play Store"