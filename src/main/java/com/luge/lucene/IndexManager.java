package com.luge.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;

/**
 * 索引库维护
 */
public class IndexManager {
    private IndexWriter indexWriter;
    @Before
    public void init() throws Exception{
        indexWriter = new IndexWriter(
                FSDirectory.open(new File("C:\\Users\\34759\\Desktop\\index").toPath()),
                new IndexWriterConfig(new IKAnalyzer()));
    }
    // 更新操作
    @Test
    public void updateDocument() throws Exception{
        Document document = new Document();
        document.add(new TextField("name", "luzan", Field.Store.YES));
        indexWriter.updateDocument(new Term("content", "luzan"), document);
        indexWriter.close();
    }

    // 添加文档
    @Test
    public void addDocument() throws Exception{
        Document document = new Document();
        document.add(new TextField("name", "new", Field.Store.YES));
        document.add(new TextField("content", "这里是一堆内容", Field.Store.YES));
        indexWriter.addDocument(document);
        indexWriter.close();
    }

    // 清空索引库
    @Test
    public void deleteAll() throws Exception{
        indexWriter.deleteAll();
        indexWriter.close();
    }
}
