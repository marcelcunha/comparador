/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mc;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marcel
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String path;
        if (args.length == 0) {
            System.out.println("Digite o caminho para pasta desejada: ");
            path = new Scanner(System.in).nextLine();
        } else {
            path = args[0];
        }

        Comparador c = new Comparador(path);

        try {
            c.percorreDiretoriosCalculaHash();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
