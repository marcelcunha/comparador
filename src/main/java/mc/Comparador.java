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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

/**
 *
 * @author marcel
 */
public class Comparador {

    private File filePath;
    private Map<String, String> fileMap;
    private List<File> fileList;

    public Comparador(String path) {
        abrirDiretorio(path);
    }

    /**
     * Inicia a variável filePath
     *
     * @param path Caminho para variável tipo File que se deseja criar
     */
    public final void abrirDiretorio(String path) {
        filePath = new File(path);
        if (!filePath.isDirectory()) {
            System.out.println("Diretório não pode ser aberto");
        }
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

    public void geraFileMap() {
        fileMap = new HashMap<>();
        fileList = new ArrayList<>(FileUtils.listFiles(filePath,
                TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE));

        int count = 0;
        for (File f : fileList) {
            fileMap.put(f.getAbsolutePath(), calculaHash(f));
            System.out.println(count);
            count++;
        }
    }

    /**
     * Dado um arquivo passado como parâmetro, seu hash é calculado
     *
     * @param f Arquivo a ser usado
     * @return String - Resultado equivalente em texto da função hash
     */
    public String calculaHash(File f) {
        byte[] fArray = null;
        String hash = null;
        try {
            fArray = Files.readAllBytes(f.toPath());
            hash = DigestUtils.md5Hex(fArray);
        } catch (IOException ex) {
            Logger.getLogger(Comparador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hash;
    }

    public void executaThread() {
        int numThreads = 6;
        fileList = new ArrayList<>(FileUtils.listFiles(filePath,
                TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE));
        fileMap = new HashMap<>();
        /*ScheduledExecutorService executor = Executors.newScheduledThreadPool(numThreads);*/

        for (int i = 0, j = 1; i < fileList.size(); i = i + numThreads, j++) {
            if (j == (int) fileList.size() / numThreads + 1) {

                fileThread(/*executor, */fileList.subList(i, fileList.size()));
            } else {
                fileThread(/*executor, */fileList.subList(i, j * numThreads - 1));
            }
        }
        /* try {
            executor.awaitTermination(0, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            Logger.getLogger(Comparador.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }

    public void fileThread(/*ScheduledExecutorService executor, */List<File> list) {
        Map<String, String> map = new HashMap<>();

        /* executor.execute(*/
        new Runnable() {
            @Override
            public void run() {
                for (File f : list) {
                    map.put(f.getAbsolutePath(), calculaHash(f));
                }
                fileMap.putAll(map);
            }
        };//);

    }

    /*public void comparar() {
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
    }*/
    public void comparar() {

        System.out.println("entrou");
        //geraFileMap();
        executaThread();
        System.out.println("saiu");
        int count = 0, size = fileMap.size();
        List<String> stringList = new ArrayList<>(fileMap.keySet());

        System.out.println(size);
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                if (stringList.get(i).equals(stringList.get(j))) {
                    System.out.println(stringList.get(i) + " = " + stringList.get(j));
                }
                count++;
            }
        }

        System.out.println(count);
    }
}
