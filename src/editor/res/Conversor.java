package editor.res;

/**
 *
 * @author Oscar Bertran <oabertran@yahoo.com.ar>
 */
public class Conversor {

    public String decToHexa(String value) {

        Double d = Double.valueOf(value);

        if (!value.matches(".*\\..*")) {

            try {
                Byte valInt = Byte.parseByte(value);
                return Integer.toHexString(0x00FF & valInt);
            } catch (NumberFormatException e) {

                throw new NumberFormatException("El valor a convertir debe estar comprendido entre -127 y 128");
            }
        } else {
            String[] floating = value.split("\\.", 2);
            Integer mantisa = Integer.parseInt(floating[0]);
            Byte signo = (byte) ((Double.parseDouble(value) < 0) ? 0x80 : 0);

            byte result = 0;
            mantisa = Math.abs(mantisa);
            if (mantisa > 0) {
                System.out.println("---------------------Mantisa mayor a 0-------------------------");
                
                int expPos;
                for (expPos = 31; expPos >= 0; expPos--) {

                    if (((mantisa >>> expPos) & 0x01) == 1) {

                        break;
                    }
                }

                System.out.println("Exponente: " + expPos);
                int frac = 0;
                Double aux = Double.parseDouble("0." + floating[1]);
                int j;
                for (j = 4 - expPos; j > 0; j--) {

                    aux = aux * 2;
                    frac = (frac << 1);
                    frac += aux.byteValue();
                    aux -= aux.byteValue();

                }
                System.out.println("Frac: " + frac);
                System.out.println("Mantisa Pre Corrimiento: " + mantisa);
                mantisa = mantisa << (4 - expPos);

                mantisa += frac;
                System.out.println("Mantisa Final antes de And: " + mantisa);

                mantisa = (mantisa & 0x0F);
                System.out.println("Mantisa Final" + mantisa);

                expPos += 4;

                if (expPos < 0 || expPos > 7) {

                    throw new NumberFormatException("El valor excede el rango de 2\u207B\u2074 y 2\u00B3");
                }
                result = (byte) (expPos << 4);
                result += mantisa;
                System.out.println("Resultado para " + value + ": " + result);
            } else {
                System.out.println(value);
                boolean end = false;
                boolean firstMatch = false;
                int frac = 0;
                int sizeFrac = 0;
                Double aux = Double.parseDouble("0." + floating[1]);
                int expNeg = 0;
                System.out.println(aux);
                while (!end) {
                    aux = aux * 2;

                    if (firstMatch) {
                        frac = (frac << 1);
                        frac += aux.byteValue();
                        sizeFrac++;
                        if (sizeFrac == 4) {

                            end = true;
                        }
                    } else {

                        expNeg++;
                    }

                    if (aux.byteValue() > 0 && !firstMatch) {
                        firstMatch = true;
                    }

                    aux -= aux.byteValue();
                System.out.println("aux "+aux);
                System.out.println("frac "+frac);

                }
                System.out.println(frac);
                expNeg = 4 - expNeg;
                if (expNeg < 0 || expNeg > 7) {

                    throw new NumberFormatException("El valor excede el rango de 2\u207B\u2074 y 2\u00B3");
                }

                result = (byte) (expNeg << 4);
                result += frac;
            }
            result += signo;
            return Integer.toHexString(0x00FF & result);

        }
    }

}
