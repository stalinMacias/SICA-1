BEGIN	
	DECLARE dtime DateTIME;
	SET dtime = CONCAT(date(now()),' ',TIME(_time));
	
	SELECT get_horario_crn.*, correosusuarios.`correo` 
	FROM get_horario_crn INNER JOIN correosusuarios ON get_horario_crn.`usuario` = correosusuarios.`usuario` and correosusuarios.`principal` = true
		
	WHERE horario >= time(_time)
        AND horario < time(_time2)
		AND anio = YEAR(dtime)
		AND ciclo = current_ciclo() 
		AND dia = current_dia()
		AND (bloque = current_bloque() OR (bloque = 0 and date(dtime) between inicio and fin))
		AND ((SELECT COUNT(1) FROM eventos WHERE DATE(dtime) BETWEEN eventos.inicio AND eventos.fin ) = 0 ) 
		AND get_horario_crn.usuario NOT IN ( 
			SELECT registrosfull.usuario FROM registrosfull 
			WHERE fechahora >= dtime - INTERVAL 20 MINUTE
			AND fechahora <= dtime + INTERVAL 20 MINUTE
			
			UNION SELECT justificantes_folios.`usuario`
				from justificantes_folios inner JOIN justificantes_periodo USING(folio)
				where justificantes_folios.`aprobado` = true
					and DATE(dtime) BETWEEN justificantes_periodo.`fecha_inicial` 
						AND justificantes_periodo.`fecha_final`)
		and get_horario_crn.`crn` not in (
			SELECT `justificantes_asignaturas`.`crn`
			FROM justificantes_folios inner JOIN justificantes_asignaturas USING(folio)
			WHERE justificantes_folios.`aprobado` = TRUE
				AND (justificantes_asignaturas.`crn` = get_horario_crn.`crn`
					AND justificantes_asignaturas.`fecha` = DATE(dtime)));	
			
    END