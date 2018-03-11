/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

/**
 *
 * @author marcel
 */
public class Comparador {

    private final String path;
    private File filePath;
    //private Map<String, byte[]> fileMap;
    private List<FileHash> fileHashList;
    private List<File> fileList; 
    
    public Comparador(String path) {
        this.path = path;
        abrirDiretorio();
        fileList = new ArrayList<>(FileUtils.listFiles(
                filePath, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE));
    }

    public final void abrirDiretorio() {
        filePath = new File(path);
    }

    /*public Map<String, byte[]> listarArquivosCalcularHash() {
        Map<String, byte[]> map = new HashMap<>();

        mapFilesHashCalc(map, filePath);
        
        return map;
    }*/
    
    public List<FileHash> listarArquivosCalcularHash() {
        List<FileHash> par = new ArrayList<>();

        listFilesHashCalc(par, filePath);
        
        return par;
    }
    public void listFilesHashCalc(List<FileHash> par, File path){
        if (path.isDirectory()) {
            File[] sub = path.listFiles();
            for (File f : sub) {
                if (f.isDirectory()) {
                    listFilesHashCalc(par, f);
                } else {
                    par.add(new FileHash(f, calculaHash(f)));
             
                }
            }
        }
    }
    
     public void listFilesHashCalc(){
         fileHashList = new ArrayList<>();
         
        for(File f: fileList){
            fileHashList.add(new FileHash(f, calculaHash(f)));
        }
    }
    /**
     * Método recursivo que adiciona ao map todos os arquivos do diretório e subdiretórios
     * junto com o Hash
     * @param map
     * @param path 
     */
    public void mapFilesHashCalc(Map<String, byte[]> map, File path){
        if (path.isDirectory()) {
            File[] sub = path.listFiles();
            for (File f : sub) {
                if (f.isDirectory()) {
                    mapFilesHashCalc(map, f);
                } else {
                    map.put(f.getAbsolutePath(), calculaHash(f));
             
                }
            }
        }
    }

    byte[] calculaHash(File f) {
        byte[] b = null;
        try {
            b = Files.readAllBytes(f.toPath());
            MessageDigest digest = MessageDigest.getInstance("MD5");

            digest.update(b, 0, b.length);
            digest.digest();
        } catch (NoSuchAlgorithmException | IOException ex) {
            Logger.getLogger(Comparador.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return b;
    }

    /*public void comparar() {
        Map<String, byte[]> map = listarArquivosCalcularHash();
        List<String> list= new ArrayList<>(map.keySet());
        System.out.println(list.size());
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
              //  System.out.println(list.get(i).getAbsolutePath() +" "+ list.get(j).getAbsolutePath());
                if(Arrays.equals(map.get(list.get(i)), map.get(list.get(j))) ){
                    
                    System.out.println(list.get(i)+" = "+list.get(j));
                    count++;
                }
            }
        }
        
        System.out.println(count);
    }*/
    
    public void comparar() {
        //List<FileHash> list = listFi;
        listFilesHashCalc();
        System.out.println(fileHashList.size());
        int count = 0;
        for (int i = 0; i < fileHashList.size(); i++) {
            for (int j = i + 1; j < fileHashList.size(); j++) {
              //  System.out.println(fileHashList.get(i).getAbsolutePath() +" "+ fileHashList.get(j).getAbsolutePath());
                if(Arrays.equals(fileHashList.get(i).getHash(), fileHashList.get(j).getHash()) ){
                    
                    System.out.println(fileHashList.get(i)+" = "+fileHashList.get(j));
                    count++;
                }
            }
        }
        
        System.out.println(count);
    }
}
