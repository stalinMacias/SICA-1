### Readme ###

Este documento es una guía rápida para que puedas configurar tu computadora
y te permita trabajar con el proyecto SiCA

## Requisitos de Software ##
JDK y JRE	---> Los puedes obtener desde la página oficial de Oracle
OneTouchBueno 	---> Este programa se encuentra en la carpeta "0 - SiCA utilerias", dependiendo la arquitectura de tu computadora es la versión que deberás instalar (x86 para 32 bits y x64 para 64 bits)
Netbeans	---> Es altamente recomendable el uso de este IDE ya que facilita el trabajo con el proyecyo
Wamp Server	---> Este software contiene php, mysql, entre otras cosas que serán utilizadas para el manejo de la base de datos de SiCA.

## Crear la base de datos ##
La base de datos puede crearse desde phpmyadmin...
1.- Creas una base de datos nueva llamada "checador"
2.- Crear un nuevo usuario con todos los permisos	--->	 usuario: frank contraseña: frankvalles65
3.- Seleccionas la base de datos que acabas de crear y te vas a la opción de importar, buscas el archivo llamado checador.sql que se encuentra en la carpeta llamada
"1 - base de datos".
4.- En la opción "Conjunto de Carácteres :	---> utf_8_general_ci

## Modificar las direcciones del host ##
Tanto en los proyectos de netbeans como en la base de datos es necesario que se modifiquen las direcciones a las que apunta el proyecto,
si se va a realizar un desarrollo de manera local, modificar todas las direcciones a: http://127.0.0.1 ... si alguna direccion original al final trae /sica,
tambien agregarselo cuando se cambie la dirección al local!
	
* Para modificar la dirección en la base de datos puedes usar el siguiente comando:
	use checador;	#Para ingresar a la base de datos
	update configuraciones set valor = "http://127.0.0.1/sica" where configuracion = "hostserver";
	select * from configuraciones; #Verificar que se haya actualizado correctamente la dirección

* Recuerda también modificar todas las direcciones en los archivos .java de los diferentes proyectos que se mencionan acontinuación.

## Distribución de los proyectos ##
En el proyecto te encontrarás con las siguientes carpetas:
	-sica2.9.1
	-sica-common
	-sica-core
	
	-sicaSpasa
	
	-SiCAWeb
	-sicaweb-core

Bueno, pues todas las carpetas deben de abrirse en el IDE Netbeans, el proyecto SicaSpasa es el encargado de cargar la base de datos de SiCA con la información de la base de datos de Spasa

Los proyectos sica2.9.1, sica-common y sica-core, los 3 en conjunto forman parte del checador....

Los proyectos SicaWeb, sica-common y sicaweb-core conforman el sistema web...

Así que dependiendo el sistema en el que vayas a trabajar serán los proyectos en los que encontrarás el codigo...

 --- 	Si al intentar correr un proyecto no realiza la conexión a la base de datos, es necesario que modifiques el metodo Configs.loadLocalConfig(reset),
que se encuentra en la siguiente ruta:
	sica-core -> sica -> SiCA.java
y modifiques el valor reset por true:	Configs.loadLocalConfig(true)
	* Vuelves a correr el programa y verificas que la conexión se haya realizado con exito.
Cierras el ejecutador y vuelves a dejar el método como se encontraba!	---


**************************	En teoría, tu computadora debería estar lista para comenzar a trabajar con el proyecto SiCA sin ningún problema	********************
