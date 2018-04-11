/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private Map<String, File> fileMap;
    private List<File> fileList;

    public Comparador(String path) {
        try {
            abrirDiretorio(path);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Comparador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Comparador() {
    }

    /**
     * Inicia a variável filePath
     *
     * @param path Caminho para variável tipo File que se deseja criar
     * @throws java.io.FileNotFoundException Lançada se o caminho forncido não
     * for de um diretório
     */
    public void abrirDiretorio(String path) throws FileNotFoundException {
        filePath = new File(path);
        if (!filePath.isDirectory()) {
            throw new FileNotFoundException("O caminho fornecido não é um diretório");
        }
    }

    /**
     * Chama o método abrirDiretorio (que verifica se o caminho é válido), em seguida,
     * chama o método percorreDiretoriosCalculaHashRec.
     * @param path Caminho para diretório a ser explorado
     */
    public void percorreDiretoriosCalculaHash(String path) {
        try {
            abrirDiretorio(path);
            percorreDiretoriosCalculaHashRec(filePath);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Comparador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Verifica se filePath foi instanciada, se sim, chama o método 
     * percorreDiretoriosCalculaHashRec, caso contrário lança uma excessão
     * @throws java.io.FileNotFoundException Lançada se o caminho forncido não
     * for de um diretório
     */
    public void percorreDiretoriosCalculaHash() throws FileNotFoundException {

        if (filePath.exists()) {
            percorreDiretoriosCalculaHashRec(filePath);
        }else{
            throw new FileNotFoundException("O caminho fornecido não é um diretório");
        }

    }

    /**
     * Método percorre a árvore de diretórios recursivamente partindo do
     * diretório informado por parâmetro. De cada arquivo encontrado é calculado
     * o hash e verificado se esse já foi adicionando no Map. Caso o hash
     * verificado se encontre no Map, significa que os arquivos correspondentes
     * são iguais.
     *
     * @param path Diretório de onde partirá a busca
     */
    public void percorreDiretoriosCalculaHashRec(File path) {
        fileMap = new HashMap<>();

        for (File f : path.listFiles()) {
            if (f.isDirectory() && f.canRead()) {                
                percorreDiretoriosCalculaHashRec(f);
            } else {
                String hash = calculaHash(f);
                if (fileMap.containsKey(hash)) {
                    System.out.println("O arquivo: " + f.getPath()
                            + " é igual a " + fileMap.get(hash).getPath());
                } else {
                    fileMap.put(hash, f);
                }

            }
        }

    }

    /**
     * Dado um arquivo passado como parâmetro, seu hash é calculado Caso o
     * arquivo seja maior que 1MB, é gerado um array com os 512KB iniciais e
     * 512KB finais contatenados. Esse array é usado para o calculo do hash.
     * Para arquivos até 1MB, o calculo é feito com o array de bytes do arquivo
     * inteiro
     *
     * @param f Arquivo a ser usado
     * @return String - Resultado equivalente em texto da função hash
     */
    public String calculaHash(File f) {
        byte[] fArray = null;
        String hash = null;

        try {
            if (f.length() > 1048576) {
                fArray = new byte[1048576];
                RandomAccessFile raf = new RandomAccessFile(f, "r");
                raf.read(fArray, 0, 524288);
                raf.seek(f.length() - 524288);
                raf.read(fArray, 524288, 524288);
                raf.close();
            } else {
                fArray = Files.readAllBytes(f.toPath());
            }

            hash = DigestUtils.md5Hex(fArray);
        } catch (IOException ex) {
            Logger.getLogger(Comparador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hash;
    }

}
