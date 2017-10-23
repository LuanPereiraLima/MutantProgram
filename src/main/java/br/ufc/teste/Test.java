package br.ufc.teste;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.omg.CORBA.ExceptionList;
import br.ufc.mutant_creator.Plano;

public class Test {

    public void amot() throws IOException {
        try {
            System.out.println("Try");
        } catch (Exception e) {
            System.out.println("Catch");
        } finally {
            System.out.println("Finally");
        }
        try {
            System.out.println("Try 2");
            throw new Plano();
        } catch (Plano e) {
            System.out.println("Catch");
        } finally {
            System.out.println("Finally 2");
        }
    }

    public static void main(String[] args) {
        try {
            new Test().amot();
        } catch (Exception e) {
        // TODO: handle exception
        }
    }
}
