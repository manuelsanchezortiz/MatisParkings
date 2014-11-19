MatisParkings
=============

Prueba Matis: Parkings

El proyecto está realizado con ItelliJ y Gradle. Dos herramientas nuevas para mí pero que me han gustado mucho
 (las probé en Android Studio y Grails) y decidí usarlas. Como veís también usa git.

Una vez clonado podéis lanzar un servidor preconfigurado con:

gradlew run

Es esta configuración predefinida escucha en el puerto 8080, localhost. Los comandos están en el contexto:

 /parksvc

Posee 5 parkings de pruebas.

Podéis ejecutar comandos con curl, por ejemplo:

curl --header "CMD_VERSION:001" http://localhost:8080/parksvc/query?count=2


Para ver todas las posibilidades podéis consultar los casos de test.