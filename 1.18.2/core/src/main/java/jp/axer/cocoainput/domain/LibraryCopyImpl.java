package jp.axer.cocoainput.domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import jp.axer.cocoainput.CocoaInput;
import org.apache.commons.io.IOUtils;

import jp.axer.cocoainput.domain.*;

public class LibraryCopyImpl implements NativeLibraryLoader {
    private SimpleLogger logger;
    private String zipsource;
    private final MinecraftNativeFolderAccessor minecraftNativeFolderAccessor;

    public LibraryCopyImpl(SimpleLogger logger, String zipsource, MinecraftNativeFolderAccessor minecraftNativeFolderAccessor) {
        this.logger = logger;
        this.zipsource = zipsource;
        this.minecraftNativeFolderAccessor = minecraftNativeFolderAccessor;
    }

    public void copyFrom(String libraryName, String libraryPath) throws IOException {
        InputStream libFile;
        if (zipsource == null) {//Fabric case
            libFile = CocoaInput.class.getResourceAsStream("/" + libraryPath);
        } else {
            try {//Modファイルを検出し、jar内からライブラリを取り出す
                ZipFile jarfile = new ZipFile(zipsource);
                libFile = jarfile.getInputStream(new ZipEntry(libraryPath));
            } catch (FileNotFoundException e) {//存在しない場合はデバッグモードであるのでクラスパスからライブラリを取り出す
                logger.log("Couldn't get library path. Is this debug mode?'");
                libFile = ClassLoader.getSystemResourceAsStream(libraryPath);
            }
        }
        File nativeDir = new File(minecraftNativeFolderAccessor.getRoot().concat("/native"));
        File copyLibFile = new File(
                minecraftNativeFolderAccessor.getRoot().concat("/native/" + libraryName));
        try {
            nativeDir.mkdir();
            FileOutputStream fos = new FileOutputStream(copyLibFile);
            copyLibFile.createNewFile();
            IOUtils.copy(libFile, fos);
            fos.close();
        } catch (IOException e1) {
            logger.error("Attempted to copy library to ./native/" + libraryName + " but failed.");
            throw e1;
        }
        System.setProperty("jna.library.path", nativeDir.getAbsolutePath());
        logger.log("CocoaInput has copied library to native directory.");
    }
}