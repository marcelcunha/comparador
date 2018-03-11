/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mc;

import java.io.File;

/**
 *
 * @author marcel
 */
public class FileHash {
    private File file;
    private byte[] hash;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public byte[] getHash() {
        return hash;
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }

    public FileHash(File file, byte[] hash) {
        this.file = file;
        this.hash = hash;
    }

    @Override
    public String toString() {
        return getFile().getAbsolutePath();
    }
    
    
}
