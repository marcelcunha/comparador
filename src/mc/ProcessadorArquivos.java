/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author Marcel
 */
public class ProcessadorArquivos implements Runnable{
    private List<File> fileList;
    private Map<String, String> fileMap;

    public ProcessadorArquivos(List<File> fileList, Map<String, String> fileMap) {
        this.fileList = fileList;
        this.fileMap = fileMap;
    }
    
    public void geraFileMap(){
        fileMap = new HashMap<>();
        
        for(File f: fileList){
            fileMap.put(f.getAbsolutePath(), calculaHash(f));
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
    
    @Override
    public void run() {
        geraFileMap();
    }
    
}
