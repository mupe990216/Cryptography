package LDN;

import java.math.BigInteger;
import java.util.*;

public class RSA {

    int tamPrimo;
    private BigInteger n, q, p;
    private BigInteger totient;
    private BigInteger e, d;

    /**
     * Constructor de la clase RSA
     */
    public RSA(int tamPrimo) {
        this.tamPrimo = tamPrimo;
        generaPrimos();             //Genera p y q
        generaClaves();             //Genera e y d

    }

    public RSA(BigInteger p, BigInteger q, int tamPrimo) {
        this.tamPrimo = tamPrimo;
        this.p = p;
        this.q = q;
        generaClaves();             //Genera e y d
    }

    public void generaPrimos() {
        p = new BigInteger(tamPrimo, 100, new Random());
        do {
            q = new BigInteger(tamPrimo, 100, new Random());
        } while (q.compareTo(p) == 0);
    }

    public void generaClaves() {
        // n = p * q
        n = p.multiply(q);
        // toltient = (p-1)*(q-1)
        totient = p.subtract(BigInteger.valueOf(1));
        totient = totient.multiply(q.subtract(BigInteger.valueOf(1)));
        // Elegimos un e coprimo de y menor que n
        do {
            e = new BigInteger(2 * tamPrimo, new Random());
        } while ((e.compareTo(totient) != -1)
                || (e.gcd(totient).compareTo(BigInteger.valueOf(1)) != 0));
        // d = e^1 mod totient
        d = e.modInverse(totient);
    }

    /*
        Funcion cifrar
    */
    public BigInteger[] cifrar(String mensaje) {
        int i;
        byte[] temp = new byte[1];
        byte[] digitos = mensaje.getBytes();
        BigInteger[] bigdigitos = new BigInteger[digitos.length];

        for (i = 0; i < bigdigitos.length; i++) {
            temp[0] = digitos[i];
            bigdigitos[i] = new BigInteger(temp);
        }

        BigInteger[] encriptado = new BigInteger[bigdigitos.length];

        for (i = 0; i < bigdigitos.length; i++) {
            encriptado[i] = bigdigitos[i].modPow(e, n);
        }

        return (encriptado);
    }
    
    /*
        Funcion Descrifrar
    */
    public String descrifrar(BigInteger[] encriptado) {
        BigInteger[] desencriptado = new BigInteger[encriptado.length];

        for (int i = 0; i < desencriptado.length; i++) {
            desencriptado[i] = encriptado[i].modPow(d, n);
        }

        char[] charArray = new char[desencriptado.length];

        for (int i = 0; i < charArray.length; i++) {
            charArray[i] = (char) (desencriptado[i].intValue());
        }

        return (new String(charArray));
    }

    public BigInteger getP() {
        return (p);
    }

    public BigInteger getQ() {
        return (q);
    }

    public BigInteger getTotient() {
        return (totient);
    }

    public BigInteger getN() {
        return (n);
    }

    public BigInteger getE() {
        return (e);
    }

    public BigInteger getD() {
        return (d);
    }
}
