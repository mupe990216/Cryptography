package Modelo;

import java.math.BigInteger;
import java.util.Random;

public class RSA {

    public BigInteger p, q, n, k, d, totient;
    public int tamPrimo;

    public RSA() {
        GeneraLlaves();
        MuestraLlaves();
    }

    public void GeneraLlaves() {
        tamPrimo = 10;
        //Genera Primos
        p = new BigInteger(tamPrimo, 100, new Random());
        do {
            q = new BigInteger(tamPrimo, 100, new Random());
        } while (q.compareTo(p) == 0);

        // n = p * q
        n = p.multiply(q);
        // toltient = (p-1)*(q-1)
        totient = p.subtract(BigInteger.valueOf(1));
        totient = totient.multiply(q.subtract(BigInteger.valueOf(1)));
        // Elegimos un k coprimo de y menor que n
        do {
            k = new BigInteger(2 * tamPrimo, new Random());
        } while ((k.compareTo(totient) != -1)
                || (k.gcd(totient).compareTo(BigInteger.valueOf(1)) != 0));
        // d = k^1 mod totient
        d = k.modInverse(totient);
    }
    
    public void MuestraLlaves(){
        System.out.println("Numero p: " + p);
        System.out.println("Numero q: " + q);
        System.out.println("Numero n: " + n);
        System.out.println("Clave k: " + k);
        System.out.println("Clave d: " + d);
        System.out.println("Clave Publica: "+k+"."+n);
        System.out.println("Clave Privada: "+d);        
    }
    
    
}
