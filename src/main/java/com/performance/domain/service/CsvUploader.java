package com.performance.domain.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.performance.domain.dao.UserDao;

@Service
public class CsvUploader {

    final static Logger log = LogManager.getLogger(CsvUploader.class);
    
    private UserDao userDao;
    
    public CsvUploader(UserDao userDao) {
        this.userDao = userDao;
    }
    
    @Async("csvUploadExecutor")
    public void csvUpload(int threadNumber) {
     // CSVを取得・CSVファイルをDBに登録する
        //ファイル読み込みで使用する3つのクラス
        InputStreamReader is = null;
        BufferedReader br = null;
        List<Object[]> csvFile = new ArrayList<Object[]>();
        try {

            //読み込みファイルのインスタンス生成
            //ファイル名を指定する
            is = new InputStreamReader(new FileInputStream(new File("data/userInfo_"+ threadNumber + ".csv")), StandardCharsets.UTF_8);
            br = new BufferedReader(is);
            

            //読み込み行
            String readLine;

            //読み込み行数の管理
            int i = 0;

            //1行ずつ読み込みを行う
            Pattern pattern = Pattern.compile(".新潟県,上越市.");
            while ((readLine = br.readLine()) != null) {
                i++;
                
              //カンマで分割した内容を配列に格納する
                Object[] data = readLine.split(",", -1);
                
                //データ内容をコンソールに表示する
                //データ件数を表示
                //配列の中身を順位表示する。列数(=列名を格納した配列の要素数)分繰り返す
                log.debug("ユーザー姓:" + data[1]);
                log.debug("出身都道府県:" + data[2]);
                log.debug("ユーザー名:" + data[0]);
                log.debug("出身市区町村:" + data[3]);
                log.debug("血液型:" + data[4]);
                log.debug("趣味1:" + data[5]);
                log.debug("趣味2:" + data[6]);
                log.debug("趣味3:" + data[7]);
                log.debug("趣味4:" + data[8]);
                log.debug("趣味5:" + data[9]);
                // 特定の件のみインサートするようにする
                Matcher matcher = pattern.matcher(readLine);
                if(matcher.find()) {
                    // 行数のインクリメント
                    i++;
                    log.info("データ書き込み" + i + "件目");
                    csvFile.add(data);
                }
            }
        } catch (Exception e) {
            log.info("csv read error", e);
        } finally {
            try {
                br.close();
            } catch (Exception e) {
            }
        }
        log.info("書き込みレコード数：" + csvFile.size());
        userDao.insertUserMaster(csvFile);
    }
}
