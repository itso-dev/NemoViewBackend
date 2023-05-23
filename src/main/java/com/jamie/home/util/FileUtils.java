package com.jamie.home.util;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.jpeg.JpegDirectory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamie.home.api.model.FILE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DecimalFormat;
import java.util.*;

public class FileUtils {

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public static FILE fileUpload(MultipartFile file, String uploadDir) {
        try {
            FILE result = new FILE();
            String uuid = UUID.randomUUID().toString();
            String oriName = file.getOriginalFilename();
            String fileType = oriName.substring(oriName.indexOf("."));
            String path = upload(uploadDir, uuid+fileType, file.getBytes());

            if(MediaUtils.getMediaType(fileType.substring(1)) != null){
                String uuid2 = UUID.randomUUID().toString();
                if(file.getSize() >= (10*1024*1024)){
                    path = uploadResizeImage(path, uploadDir, uuid2+fileType, 0.1f);
                } else if(file.getSize() >= (5*1024*1024)){
                    path = uploadResizeImage(path, uploadDir, uuid2+fileType, 0.2f);
                } else {
                    path = uploadResizeImage(path, uploadDir, uuid2+fileType, 0.3f);
                }
            }

            result.setName(file.getOriginalFilename());
            result.setUuid(uuid);
            result.setPath(path);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String upload(String uploadPath, String originalName, byte[] fileData)throws Exception{

        String savedPath = calcPath(uploadPath);

        File target = new File(uploadPath + savedPath, originalName);

        FileCopyUtils.copy(fileData, target);

        return savedPath + File.separator + originalName;
    }

    public static String uploadResizeImage(String path, String uploadPath, String originalName, float targetQuality)throws Exception{

        String savedPath = calcPath(uploadPath);

        // 1. 원본 파일을 읽는다.
        File imageFile = new File(uploadPath, path);

        // 2. 원본 파일의 Orientation 정보를 읽는다.
        int orientation = 1; // 회전정보, 1. 0도, 3. 180도, 6. 270도, 8. 90도 회전한 정보
        int width = 0; // 이미지의 가로폭
        int height = 0; // 이미지의 세로높이
        int tempWidth = 0; // 이미지 가로, 세로 교차를 위한 임의 변수

        Metadata metadata = ImageMetadataReader.readMetadata(imageFile); // 이미지 메타 데이터 객체
        Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class); // 이미지의 Exif 데이터를 읽기 위한 객체
        JpegDirectory jpegDirectory = metadata.getFirstDirectoryOfType(JpegDirectory.class); // JPG 이미지 정보를 읽기 위한 객체

        if(directory != null){
            orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION); // 회전정보
            width = jpegDirectory.getImageWidth(); // 가로
            height = jpegDirectory.getImageHeight(); // 세로
        }

        // 3. 변경할 값들을 설정한다.
        AffineTransform atf = new AffineTransform();
        switch (orientation) {
            case 1:
                break;
            case 2: // Flip X
                atf.scale(-1.0, 1.0);
                atf.translate(-width, 0);
                break;
            case 3: // PI rotation
                atf.translate(width, height);
                atf.rotate(Math.PI);
                break;
            case 4: // Flip Y
                atf.scale(1.0, -1.0);
                atf.translate(0, -height);
                break;
            case 5: // - PI/2 and Flip X
                atf.rotate(-Math.PI / 2);
                atf.scale(-1.0, 1.0);
                break;
            case 6: // -PI/2 and -width
                atf.translate(height, 0);
                atf.rotate(Math.PI / 2);
                break;
            case 7: // PI/2 and Flip
                atf.scale(-1.0, 1.0);
                atf.translate(-height, 0);
                atf.translate(0, width);
                atf.rotate(  3 * Math.PI / 2);
                break;
            case 8: // PI / 2
                atf.translate(0, width);
                atf.rotate(  3 * Math.PI / 2);
                break;
        }

        switch (orientation) {
            case 5:
            case 6:
            case 7:
            case 8:
                tempWidth = width;
                width = height;
                height = tempWidth;
                break;
        }

        BufferedImage image = ImageIO.read(imageFile);
        final BufferedImage afterImage = new BufferedImage(width, height, image.getType());
        final AffineTransformOp rotateOp = new AffineTransformOp(atf, AffineTransformOp.TYPE_BILINEAR);
        final BufferedImage rotatedImage = rotateOp.filter(image, afterImage);
        Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpg");
        ImageWriter writer = iter.next();
        ImageWriteParam iwp = writer.getDefaultWriteParam();
        iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        iwp.setCompressionQuality(targetQuality);

        // 4. 회전하여 생성할 파일을 만든다.
        File outFile = new File(uploadPath + savedPath, originalName);
        FileImageOutputStream fios = new FileImageOutputStream(outFile);

        // 5. 원본파일을 회전하여 파일을 저장한다.
        writer.setOutput(fios);
        writer.write(null, new IIOImage(rotatedImage ,null,null),iwp);
        fios.close();
        writer.dispose();

        // 6. 원본파일 삭제
        deleteFile(path,uploadPath);

        return savedPath + File.separator + originalName;
    }

    private static String calcPath(String uploadPath){

        Calendar cal = Calendar.getInstance();

        String yearPath = File.separator+cal.get(Calendar.YEAR);

        String monthPath = yearPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.MONTH)+1);

        String datePath = monthPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.DATE));

        makeDir(uploadPath, yearPath, monthPath, datePath);

        logger.info(datePath);

        return datePath;
    }

    private static void makeDir(String uploadPath, String... paths){

        if(new File(paths[paths.length-1]).exists()){
            return;
        }

        for (String path : paths) {

            File dirPath = new File(uploadPath + path);

            if(! dirPath.exists() ){
                dirPath.mkdir();
            }
        }
    }

    public static void deleteFile(String fileName, String uploadDir) throws Exception {

        logger.info("fileDelete start.....");

        new File(uploadDir + fileName.replace('/', File.separatorChar)).delete();
    }

    public static String saveFiles(ArrayList<MultipartFile> saveFiles, String uploadDir) throws Exception {
        logger.info("saveFiles start.....");
        String result = "[]";
        if(saveFiles != null){
            List<FILE> fileList = new ArrayList<>();
            for(MultipartFile file : saveFiles){
                if(file.getSize() != 0){
                    FILE fileVo = fileUpload(file, uploadDir);
                    fileList.add(fileVo);
                }
            }

            if(fileList.size() != 0){
                result = mapper.writeValueAsString(fileList);
            } else {
                // 모든 input file에서 파일을 선택하지 않은경우
            }
        } else {
            // file input이 없는 경우
        }

        return result;
    }

    public static String modiFiles(String files, String deleteFiles, ArrayList<MultipartFile> saveFiles, String uploadDir) throws Exception{
        String result = "[]";
        // 사진 수정
        List<FILE> fileList = new ArrayList<>();
        if(files != null){
            fileList = Arrays.asList(mapper.readValue(files, FILE[].class));
        }
        List<FILE> newFileList = new ArrayList<>();
        if(deleteFiles != null){
            // 기존 파일 삭제
            List<FILE> delFiles = Arrays.asList(mapper.readValue(deleteFiles, FILE[].class));
            for(FILE delFile : delFiles){
                deleteFile(delFile.getPath(),uploadDir);
            }
        }

        for(FILE file : fileList){
            newFileList.add(file);
        }

        if(saveFiles != null){
            for(MultipartFile file : saveFiles){
                if(file.getSize() != 0){
                    FILE fileVo = FileUtils.fileUpload(file, uploadDir);
                    newFileList.add(fileVo);
                }
            }
        }

        if(newFileList.size() != 0){
            result = mapper.writeValueAsString(newFileList);
        } else {
            // 파일을 다 삭제하고 추가하지 않은 경우
        }
        return result;
    }
}
