package com.chaluutali.chirwa.metricimperialconverter.unit.web.service.converter;

import com.chaluutali.chirwa.metricimperialconverter.unit.web.enums.AreaUnitsOfMeasure;
import com.chaluutali.chirwa.metricimperialconverter.unit.web.model.Conversion;
import com.chaluutali.chirwa.metricimperialconverter.unit.web.model.ConversionResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

@Service
public class AreaConverter {

    public ResponseEntity<ConversionResult> convert(final Conversion conversion) {
        final AreaUnitsOfMeasure unitFrom = Arrays.stream(AreaUnitsOfMeasure.values())
                .filter(unit -> unit.getUnit().equals(conversion.getConvertUnitFrom()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Unit Not Allowed : " + conversion.getConvertUnitFrom()));
        return process(unitFrom, conversion);
    }

    private ResponseEntity<ConversionResult> process(final AreaUnitsOfMeasure unitFrom, final Conversion conversion) {
        return new ResponseEntity<>(ConversionResult.builder()
                .result(calculate(unitFrom, conversion))
                .unit(conversion.getConvertUnitTo())
                .build(), HttpStatus.OK);
    }

    private double calculate(final AreaUnitsOfMeasure unitFrom, final Conversion conversion) {
        if (conversion.getConvertUnitFrom().equals(conversion.getConvertUnitTo())) {
            return conversion.getInputValue();
        }
       final AreaUnitsOfMeasure unitTo = Arrays.stream(AreaUnitsOfMeasure.values())
               .filter(unit -> unit.getUnit().equals(conversion.getConvertUnitTo()))
               .findFirst()
               .orElseThrow(() -> new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Unit Not Allowed : " + conversion.getConvertUnitTo()));
       return doConversion(unitFrom, unitTo, conversion);
    }

    private double doConversion(final AreaUnitsOfMeasure unitFrom,final AreaUnitsOfMeasure unitTo,final Conversion conversion) {
        return (conversion.getInputValue() / unitFrom.getDefaultValue()) * unitTo.getDefaultValue();
    }
}
