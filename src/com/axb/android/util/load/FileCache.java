package com.axb.android.util.load;

import java.io.File;

import android.content.Context;

public class FileCache {
	private File cacheDir;
	
    public FileCache(File cacheDir){
       this.cacheDir = cacheDir;
    }
    
    /**
     * �õ��ļ�����
     * @param url
     * @return
     */
    public File getFile(String url){
        String filename=String.valueOf(url.hashCode());
        File f = new File(cacheDir, filename);
        return f;
        
    }
    
    /**
     * ɾ������ �ļ����������ļ�
     */
    public void clear(){
        File[] files=cacheDir.listFiles();
        if(files==null)
            return;
        for(File f:files)
            f.delete();
    }

}