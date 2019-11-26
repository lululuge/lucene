package com.luge.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;

/**
 * 索引库查询
 */
public class IndexSearch {
    private IndexReader indexReader;
    private IndexSearcher indexSearcher;
    private void printResult(Query query) throws Exception{
        TopDocs topDocs = indexSearcher.search(query, 10);
        System.out.println("总记录数：" + topDocs.totalHits);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc doc : scoreDocs) {
            int docId = doc.doc;
            Document document = indexSearcher.doc(docId);
            System.out.println(document.get("name"));
            System.out.println(document.get("path"));
            System.out.println(document.get("size"));
            System.out.println("-----------------");
        }
        indexReader.close();
    }
    @Before
    public void init() throws Exception{
        indexReader = DirectoryReader.open(FSDirectory.open(new File("C:\\Users\\34759\\Desktop\\index").toPath()));
        indexSearcher = new IndexSearcher(indexReader);
    }

    @Test
    public void testRangeQuery() throws Exception{
        // 创建一个Query对象
        Query query = LongPoint.newRangeQuery("size", 0l, 10000l);
        printResult(query);
    }

    @Test
    public void testTermQuery() throws Exception{
        Query query = new TermQuery(new Term("content", "spring"));
        printResult(query);
    }

    @Test
    public void testQueryParser() throws Exception{
        //  创建QueryParser对象
        // 参数一：要查询的域，参数二：分析器对象
        QueryParser queryParser = new QueryParser("content", new IKAnalyzer());
        Query query = queryParser.parse("lucene是JAVA开发的全文检索工具包");
        printResult(query);
    }
}
