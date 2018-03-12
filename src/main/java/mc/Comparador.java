/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author marcel
 */
public class Comparador {

    private final String path;
    private File filePath;
    private Map<String, String> fileMap;
   

    public Comparador(String path) {
        this.path = path;
        abrirDiretorio();
    }

    /**
     * Inicia a variável filePath
     */
    public final void abrirDiretorio() {
        filePath = new File(path);
    }

    public void percorrerDiretoriosCalcularHash() {
        fileMap = new HashMap<>();

        percorrerDiretoriosCalcularHashRec(filePath);
    }

    /**
     * Método percorre a árvore de diretórios recursivamente partindo do
     * diretório informado por parâmetro. Cada arquivo encontrado é adicionado a
     * variável fileMap junto a uma string correspondente ao seu valor hash
     *
     * @param path Diretório de partida para método recursivo
     */
    private void percorrerDiretoriosCalcularHashRec(File path) {
        if (path.isDirectory()) {
            File[] sub = path.listFiles();
            for (File f : sub) {
                if (f.isDirectory()) {
                    percorrerDiretoriosCalcularHashRec(f);
                } else {
                    fileMap.put(path.getAbsolutePath(), calculaHash(f));
                }
            }
        }
    }

    /**
     * Dado um arquivo passado como parâmetro, seu hash é calculado
     *
     * @param f Arquivo a ser usado
     * @return <String> Resultado equivalente em texto da função hash
     */
    public String calculaHash(File f) {
        byte[] fArray = null;
        String hash = null;
        try {
            fArray = Files.readAllBytes(f.toPath());
            hash = DigestUtils.sha1Hex(fArray);
        } catch (IOException ex) {
            Logger.getLogger(Comparador.class.getName()).log(Level.SEVERE, null, ex);
        }

        return hash;
    }

    public void comparar() {
        percorrerDiretoriosCalcularHash();
        
        int count = 0, size = fileMap.size();
        List<String> stringList = new ArrayList<>(fileMap.keySet());
        
        System.out.println(size);
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                if(stringList.get(i).equals(stringList.get(j))){
                    System.out.println(stringList.get(i) +" = "+ stringList.get(j));
                }
            }
        }

        System.out.println(count);
    }
}
