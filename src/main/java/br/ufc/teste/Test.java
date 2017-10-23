package br.ufc.teste;

import java.io.IOException;

public class Test {

    public void amot() throws Exception, IOException {
        try {
            System.out.println("Try");
        } catch (Exception e) {
            System.out.println("Catch");
        } finally {
            System.out.println("Finally");
        }
        try {
            System.out.println("Try 2");
        } catch (Exception e) {
            System.out.println("Catch");
        } finally {
            System.out.println("Finally 2");
        }
    }

    public static void main(String[] args) {
        try {
            new Test().amot();
        }catch (Exception e) {
			// TODO: handle exception
		}
    }
}
