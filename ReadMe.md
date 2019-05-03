# ProjectZ

Gitlab: https://gitlab.com/Mussiam/ProjectZ/

# What's ProjectZ ?

It's a Dragon Ball radar with augmented reality.

ProjectZ is a app developed for android in android studio. This application run with the API 22 minimum.
The connection to the API is mandatory. The connection to the API is in localhost for test purpose so you need to be in the same network (Android & PC).
Withou the connection to the API you can search the Ball.

# Technologies:

- Application (Android Studio):
	- Java
	- Kudan (Augmented Reality)
	- Unity 3D

- WebService Rest with WampServer(64bits):
	- Php	
	- MySQL
	- Apache

# How to install and configure the API in WampServer?
- Go to this link to download Wampserver:		
	http://www.wampserver.com/
	
- After that install it run the .exe dowloaded, a file like this:	
	wampserver3.0.6_x64_apache2.4.23_mysql5.7.14_php5.6.25-7.0.10
	
- You can install it everywhere you want, but let in by defaults in C:/ 
	you have your wamp at this path: C:\wamp64 or C:\wamp
	
- Go to :
	https://gitlab.com/Mussiam/ProjectZ/Webservice/

- Copy the directory ProjectZ in Webservice/ and paste it to :
	C:\wamp64\www\ or C:\wamp\www\

- Launch WampServer 
	http://localhost in your browser and check if you have in Your project,ProjectZ

- Launch phpMyAdmin
	- http://localhost/phpmyadmin/ in your browser
	- Username:root
	- Password:              (nothing needed)

- Use Import Database:
	Path to import Database:	C:\wamp64\www\ProjectZ\database schema with sample data or C:\wamp\www\ProjectZ\database schema with sample data

- Now in my phpMyAdmin if you check in the structure you see projectz with a database ballz

- You can test the API by going in you browser and tap : 
	http://localhost/projectz/index.php?key=\*ProjectZ*

- For testing purpose you need to connect the API with the application you need to be in the same network.

- In the computer where wamp server is running open the console and type : ipconfig then search for yor ipv4
		
- Once you have the ip adress of your local server, open the application and go to setting, then write the ip adress 
	
- Click on TEST CONNECTION API, if everything is working fine u should get the message 'connection ok'
