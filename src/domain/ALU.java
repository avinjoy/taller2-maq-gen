package domain;

import static java.lang.Math.pow;

/**
 *
 * @author Oscar Bertran <oabertran@yahoo.com.ar>
 */
public class ALU {

    private Integer overflow = 0;

    private Integer carry = 0;

    private Integer presicion = 0;

    public Integer getPresicion() {
        return presicion;
    }

    private static ALU alu = new ALU();

    public Integer getOverflow() {
        return overflow;
    }

    public Integer getCarry() {
        return carry;
    }

    public void setOverflow(Integer overflow) {
        this.overflow = overflow;
    }

    public void setCarry(Integer carry) {
        this.carry = carry;
    }

    public Byte addInteger(Byte value1, Byte value2) {

        overflow = 0;
        carry = 0;

        Integer result = value1 + value2;

        if (((value1 & 0x80) == (value2 & 0x80)) && (((value1 & 0x80) != (result & 0x80)) || ((value2 & 0x80) != (result & 0x80)))) {

            overflow = 1;
        }

        if ((result & 0x100) == 0x100) {

            carry = 1;
        }

        return result.byteValue();
    }

    public Byte addFloat(Byte value1, Byte value2) {
        presicion = 0;
        overflow = 0;
        System.out.println("************** Suma de " + Integer.toHexString((byte) value1) + " + " + Integer.toHexString(0x0FF & value2) + " *******************");
        Byte sumando1 = value1;
        Byte sumando2 = value2;
        if (((value1 >>> 4) & (0x07)) < ((value2 >>> 4) & (0x07))) {
            sumando1 = value2;
            sumando2 = value1;
            System.out.println("Sumando 1 menor que sumando 2");
        }

        byte exp1 = (byte) ((sumando1 >>> 4) & (0x07));
        byte exp2 = (byte) ((sumando2 >>> 4) & (0x07));
        byte signo1 = (byte) ((sumando1 & 0x80) >>> 7);
        exp1 += -4;
        byte mantisa1 = (byte) (sumando1 & 0x0f);
        byte signo2 = (byte) ((sumando2 & 0x80) >>> 7);
        exp2 += -4;
        byte mantisa2 = (byte) (sumando2 & 0x0f);
        int mantisa1Int;
        int mantisa2Int;
        int mantisaResult;
        byte signoFinal = 0;
        byte resultFinal = 0;
        mantisa2Int = mantisa2 | 0x10;
        int suffix;
        suffix = mantisa2Int & (int) (Math.pow(2, (exp1 - exp2)) - 1);
        if (suffix > 0) {

            presicion = 1;
            System.out.println("Presicion Perdida 1: " + suffix);
        }
        mantisa2Int = (mantisa2Int >>> (exp1 - exp2));
        mantisa1Int = mantisa1 | 0x10;
        System.out.println("--------------Numero 1-----------------");
        System.out.println("Signo: " + signo1);
        System.out.println("Mantisa: " + mantisa1);
        System.out.println("Exp: " + exp1);
        System.out.println("--------------Numero 2-----------------");
        System.out.println("Signo: " + signo2);
        System.out.println("Mantisa: " + mantisa2);
        System.out.println("Exp: " + exp2);
        System.out.println("--------------Pre-Suma-----------------");

        if (signo1 > 0) {
            System.out.println("Mantisa1 Pre Invertir: " + mantisa1Int);
            mantisa1Int = (~mantisa1Int) + 0x01;
        }
        if (signo2 > 0) {

            System.out.println("Mantisa2 Pre Invertir: " + mantisa2Int);
            mantisa2Int = (~mantisa2Int) + 0x01;
        }

        System.out.println("Mantisa1: " + mantisa1Int);
        System.out.println("Mantisa2: " + mantisa2Int);
        mantisaResult = mantisa1Int + mantisa2Int;

        if (mantisaResult < 0) {

            System.out.println("Mantisa Result Pre Invertir: " + mantisa1Int);
            mantisaResult = (~mantisaResult) + 1;
            signoFinal = 1;
        }

        int expPos;
        int expFinal;
        for (expPos = 31; expPos >= 0; expPos--) {

            if (((mantisaResult >>> expPos) & 0x01) == 1) {

                break;
            }
        }
        //0011.0101.0101 
        expFinal = expPos + exp1 - Byte.SIZE / 2;
        System.out.println("--------------Resultado-----------------");
        System.out.println("Mantisa Resultado Pre correr: " + mantisaResult);
        if (expPos > 4) {
            
            suffix = mantisaResult & (int) (Math.pow(2, (expPos - 4)) - 1);
            if (suffix > 0) {

                presicion = 1;
                System.out.println("Presicion perdida 2: " + suffix);
            }
            mantisaResult = mantisaResult >>> (expPos - 4);
        } else {

            mantisaResult = mantisaResult << (4 - expPos);
        }
        
        if (expFinal > 3 || expFinal<-4){
            
            overflow = 1;
        }
        System.out.println("Exponente Relativo: " + expPos);
        System.out.println("Exponente Final: " + expFinal);
        System.out.println("Mantisa Resultado: " + mantisaResult);
        resultFinal = (byte) (signoFinal << 7);
        System.out.println("Resultado con signo: " + resultFinal);
        resultFinal = (byte) (resultFinal | ((expFinal + 4) << 4));
        System.out.println("Resultado con signo + exponente: " + resultFinal);
        resultFinal = (byte) (resultFinal | (mantisaResult & 0x0F));
        System.out.println("Resultado final: " + resultFinal);

        return resultFinal;

    }

    public static ALU ALU() {

        return alu;
    }
}
