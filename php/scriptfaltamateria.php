<?php //Codigo por Set Martinez, resuelve el envio de correos por faltas a clase
	date_default_timezone_set("America/Mexico_City");	
  $hora = ((int)date("H")) - 2;
	//$hora = $argv[1];
	//echo $hora;




	if ($hora==8 || $hora==10 || $hora==12 || $hora==14 || $hora==16 || $hora==18){
		include("conectar.php");
		include("class.phpmailer.php");
		include("class.smtp.php");

		$query = "CALL get_faltas_clases_hora('$hora:00:00')";
        set_time_limit(150);
		//echo $query;

		if ($result = mysqli_query($con, $query)) {
			$today = date("d/m/Y");

			while($row = mysqli_fetch_assoc($result)) {

				echo $row['nombre'].": ";
				echo $hora."<br>";
				//echo var_dump($row);

				if (filter_var($row["correo"], FILTER_VALIDATE_EMAIL) && $row['crn'] != 147450 && $row['crn'] != 145069 ) {
				    //enviar email

				  $mail = new PHPMailer();
					$mail->IsSMTP();
					$mail->SMTPAuth = false;
					//$mail->SMTPSecure = "ssl";

					$mail->Host = MAIL_HOST;
					$mail->Port = MAIL_PORT;
					$mail->Username = MAIL_USERNAME;
					$mail->Password = MAIL_PASSWORD;
					$mail->SMTPAuth = true;
					$mail->SMTPSecure = 'tls';

					$mail->SetFrom (MAIL_USERNAME,"Sistema de Control de Asistencia(SiCA) CUSUR");

					$mail->Subject = "CUSUR SICA - Falta a asignatura";
					$mail->AltBody = "Correo de incidencia";

					$mensaje = "<div align='justified'>	<p><b>Acad&eacute;mico $row[nombre] </b></p>
						<p align='justify'>Se ha generado una falta en el sistema el d&iacute;a <b>$row[dia] $today</b>, en la materia <b>$row[materia] ($row[crn])</b> registrada en el horario <b>$row[horario] hrs.</b> por incidencia en registro de <b>entrada o salida</b>. Deber&aacute; acudir con su Jefe de Departamento a efectuar la justificaci&oacute;n correspondiente; se le recuerda que tiene 5 d&iacute;as h&aacute;biles para presentarla, de lo contrario se proceder&aacute; conforme a la normatividad.</p>
						<p align='justify' style='color:grey;'>Este es un correo autom&aacute;tico generado por el Sistema de Control de Asistencia (SICA) del Centro Universitario de los Valles de la Universidad de Guadalajara. No es necesario que responda a este correo.</p>
						<p>Sistema de Control de Asistencia, CUSUR.</p></div>";

					$mail->MsgHTML($mensaje);
					$mail->IsHTML(true);
					$mail->AddAddress($row["correo"],$row["nombre"]);
					$mail->AddCC("buzonsica@valles.udg.mx","SiCA");
					//$mail->AddBCC('carmen.hernandez@valles.udg.mx');

					$flag = false;

					for ($i=1 ; $i<4 && !$flag ; $i++){
						set_time_limit(60);
						$flag = $mail->Send();
						if(!$flag) {
							echo "Error: " . $mail->ErrorInfo."</br>";
						} else {
						   echo $row['nombre'];
							echo "Mensaje enviado correctamente</br>";
						}
					}


				} else {
					echo "Error: usuario no cuenta con correo electronico o este es invalido</br>";
				}
			}
			mysqli_free_result($result);

		} else {
			echo mysqli_error($con);
		}
	} else {
		echo $hora.":00:00";
	}
?>
