Android - SpeedTest.net Data Visualizer
=======================================

[Speedtest.net android application][applink] data visualization using Google Maps and other means (in works).


This is a very simple data visualization app, which currently shows points where speed test was done and associated information with it.

Why this?
==========
Though Speedtest.net android app records location information with the test data, there is currently no way to visualize the data in map. This is an effort to fill this gap, and possibly implement other ways to generate chart to understand even more on data stored by the app.  


How to use app
=============== 
This app depends on exported data from [Speedtest.net app][applink]. Currently there are 2 ways to input data into this visualizer app:
 1. By launching Speedtest.net app, and use existing export feature.
 2. Or, use already exported data - just copy data and paste into input box 

TODO: Add video URL


Known issues
==============
 * Maps camera does not update bounds to show all marker points after changing "Connection Type" filter.


Possible features
====================
Here are list of nice to have *(missing)* features, which can be implemented in future release for better visualization.

 * Show chart containing all test records
 * Filter records by date range
 * Filter records by download, upload or latency speed
 * Filter by region


Contributing to project
=========================
If you would like to contribute to this project, please create pull request. We'll be happy to add new features to this app.

Here is [how to setup project](https://github.com/liquidlabs/android-speedtest-mapper/wiki/How-to-setup-project "How to setup project") with eclipse.


Credits
=======
 * Apache Commons Lang3 - Utility classes for Java - http://commons.apache.org/proper/commons-lang/
 * Apache Commons CSV - parser used to parse CSV data from SpeedTest.net app - http://commons.apache.org/proper/commons-csv/
 * Google Maps V2 - https://developers.google.com/maps/documentation/android/
 * GraphView by by Jonas Gehring - https://github.com/jjoe64/GraphView
 
License
========
This app is licensed under *Apache License, Version 2.0* - http://www.apache.org/licenses/LICENSE-2.0

Privacy
========
This app does *NOT* collect any personal identifiable information.
App does not store data used to visualize exported data. Once app exits, data is removed from memory.



[applink]: https://play.google.com/store/apps/details?id=org.zwanoo.android.speedtest "Speedtest.net Android App at Google Play Store"