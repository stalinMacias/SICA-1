### Readme ###

Este documento es una gu�a r�pida para que puedas configurar tu computadora
y te permita trabajar con el proyecto SiCA

## Requisitos de Software ##
JDK y JRE	---> Los puedes obtener desde la p�gina oficial de Oracle
OneTouchBueno 	---> Este programa se encuentra en la carpeta "0 - SiCA utilerias", dependiendo la arquitectura de tu computadora es la versi�n que deber�s instalar (x86 para 32 bits y x64 para 64 bits)
Netbeans	---> Es altamente recomendable el uso de este IDE ya que facilita el trabajo con el proyecyo
Wamp Server	---> Este software contiene php, mysql, entre otras cosas que ser�n utilizadas para el manejo de la base de datos de SiCA.

## Crear la base de datos ##
La base de datos puede crearse desde phpmyadmin...
1.- Creas una base de datos nueva llamada "checador"
2.- Crear un nuevo usuario con todos los permisos	--->	 usuario: frank contrase�a: frankvalles65
3.- Seleccionas la base de datos que acabas de crear y te vas a la opci�n de importar, buscas el archivo llamado checador.sql que se encuentra en la carpeta llamada
"1 - base de datos".
4.- En la opci�n "Conjunto de Car�cteres :	---> utf_8_general_ci

## Modificar las direcciones del host ##
Tanto en los proyectos de netbeans como en la base de datos es necesario que se modifiquen las direcciones a las que apunta el proyecto,
si se va a realizar un desarrollo de manera local, modificar todas las direcciones a: http://127.0.0.1 ... si alguna direccion original al final trae /sica,
tambien agregarselo cuando se cambie la direcci�n al local!

* Para modificar la direcci�n en la base de datos puedes usar el siguiente comando:
	use checador;	#Para ingresar a la base de datos
	update configuraciones set valor = "http://127.0.0.1/sica" where configuracion = "hostserver";
	select * from configuraciones; #Verificar que se haya actualizado correctamente la direcci�n

* Recuerda tambi�n modificar todas las direcciones en los archivos .java de los diferentes proyectos que se mencionan acontinuaci�n.

## Distribuci�n de los proyectos ##
En el proyecto te encontrar�s con las siguientes carpetas:
	-sica2.9.1
	-sica-common
	-sica-core

	-sicaSpasa

	-SiCAWeb
	-sicaweb-core

Bueno, pues todas las carpetas deben de abrirse en el IDE Netbeans, el proyecto SicaSpasa es el encargado de cargar la base de datos de SiCA con la informaci�n de la base de datos de Spasa

Los proyectos sica2.9.1, sica-common y sica-core, los 3 en conjunto forman parte del checador....

Los proyectos SicaWeb, sica-common y sicaweb-core conforman el sistema web...

As� que dependiendo el sistema en el que vayas a trabajar ser�n los proyectos en los que encontrar�s el codigo...

 --- 	Si al intentar correr un proyecto no realiza la conexi�n a la base de datos, es necesario que modifiques el metodo Configs.loadLocalConfig(reset),
que se encuentra en la siguiente ruta:
	sica-core -> sica -> SiCA.java
y modifiques el valor reset por true:	Configs.loadLocalConfig(true)
	* Vuelves a correr el programa y verificas que la conexi�n se haya realizado con exito.
Cierras el ejecutador y vuelves a dejar el m�todo como se encontraba!	---


**************************	En teor�a, tu computadora deber�a estar lista para comenzar a trabajar con el proyecto SiCA sin ning�n problema	********************

## Update SiCA DataBase ##
Dados los datos que actualmente existen en SPASA (30/09/2019) se debe de actualizar la longitud del atributo "aula" en la tabla "horarioscrn", de lo contrario cuando se ejecute el script para sincronizar SPASA con SiCA se genererarán errores....
	* Recomendado actualizar la longitud a varchar(15), al clonar la base viene con varchar(5)
