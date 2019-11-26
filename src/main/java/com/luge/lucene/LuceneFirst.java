package com.luge.lucene;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.*;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

/**
 * 入门案例
 */
public class LuceneFirst {
    @Test
    public void createIndex() throws Exception {
        // 1.创建一个Directory对象，指定索引库保存位置
//        Directory directory = new RAMDirectory(); 把索引库保存在内存中
        Directory directory = FSDirectory.open(new File("C:\\Users\\34759\\Desktop\\index").toPath()); // 把索引库保存在磁盘中
        // 2.基于Directory对象创建一个IndexWriter对象
        IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig()); // 两个参数不要忘记
        // 3.读取磁盘上的文件，对应每个文件创建一个文档对象
        File dir = new File("C:\\Users\\34759\\Desktop\\source");
        File[] files = dir.listFiles();
        for (File file : files) {
            // 获取文件名
            String fileName = file.getName();
            // 获取文件路径
            String filePath = file.getPath();
            // 获取文件内容
            String fileContent = FileUtils.readFileToString(file, "utf-8");
            // 获取文件大小
            long fileSize = FileUtils.sizeOf(file);
            // 创建域
            // 参数1：域名，参数2：域值，参数3：是否存储
            Field fieldName = new TextField("name", fileName, Field.Store.YES);
            Field fieldPath = new TextField("path", filePath, Field.Store.YES);
            Field fieldContent = new TextField("content", fileContent, Field.Store.YES);
//            Field fieldSize = new TextField("size", fileSize + "", Field.Store.YES);
            Field fieldSizeValue = new LongPoint("size", fileSize);
            Field fieldSizeStore = new StoredField("size", fileSize);
            // 创建文档对象
            Document document = new Document();
            // 4.向文档对象中添加域
            document.add(fieldName);
            document.add(fieldPath);
            document.add(fieldContent);
//            document.add(fieldSize);
            document.add(fieldSizeValue);
            document.add(fieldSizeStore);
            // 5.把文档对象写入索引库
            indexWriter.addDocument(document);
        }
        // 6.关闭IndexWriter对象
        indexWriter.close();
    }

    @Test
    public void searchIndex() throws Exception{
        Directory directory = FSDirectory.open(new File("C:\\Users\\34759\\Desktop\\index").toPath());
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        Query query = new TermQuery(new Term("name", "apache"));
        TopDocs topDocs = indexSearcher.search(query, 10);
        System.out.println("查询总记录数：" + topDocs.totalHits);
        // 取文档列表
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc doc : scoreDocs) {
            // 取文档id
            int docId = doc.doc;
            // 根据id取文档对象
            Document document = indexSearcher.doc(docId);
            // 打印文档内容
            System.out.println(document.get("name"));
            System.out.println(document.get("path"));
            System.out.println(document.get("size"));
//            System.out.println(document.get("content"));
            System.out.println("-------------------------");
        }
        indexReader.close();
    }

    /**
     * 查看分析器的分词效果
     * @throws Exception
     */
    @Test
    public void testTokenStream() throws Exception {
        Analyzer analyzer = new IKAnalyzer();
        TokenStream tokenStream = analyzer.tokenStream("", "lucene是JAVA开发的全文检索工具包");
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        tokenStream.reset();
        while (tokenStream.incrementToken()) {
            System.out.println(charTermAttribute.toString());
        }
        tokenStream.close();
    }
}
