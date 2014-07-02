package com.axb.android.util.load;

import java.io.File;

public class FileCache {
	private File cacheDir;
	
    public FileCache(File cacheDir){
       this.cacheDir = cacheDir;
    }
    
    /**
     * 得到文件对象
     * @param url
     * @return
     */
    public File getFile(String url){
        String filename=String.valueOf(url.hashCode());
        File f = new File(cacheDir, filename);
        return f;
        
    }
    
    /**
     * 删除缓存 文件夹下所有文件
     */
    public void clear(){
        File[] files=cacheDir.listFiles();
        if(files==null)
            return;
        for(File f:files)
            f.delete();
    }

}