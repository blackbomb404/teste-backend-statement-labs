package com.statementlabs.BackEndTest.application.services.impl;

import com.statementlabs.BackEndTest.application.services.CodigoSeloService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CodigoSeloServiceImpl implements CodigoSeloService {
    @Override
    public String gerarCodigo() {
        var anoActual = LocalDate.now().getYear();
        var sequenciaFormatada = String.format("%06d", GeradorCodigo.proximoValorSequencia++);

        var novoCodigo = "PROSEFA-" + anoActual + "-" + sequenciaFormatada;
        GeradorCodigo.ultimoCodigoGerado = novoCodigo;
        return novoCodigo;
    }

    private static class GeradorCodigo {
        public static long proximoValorSequencia = 1L;
        public static String ultimoCodigoGerado = "";

        public static String extrairAno(String input) {
            String regex = "^PROSEFA-(\\d{4})-\\d{6}$";

            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(input);

            if (matcher.matches()) {
                return matcher.group(1);
            } else {
                return null;
            }
        }
    }
}
