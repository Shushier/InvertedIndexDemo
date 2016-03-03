/*
Author:郭源
2013-03-24

需求：为文件建立倒排索引
step1：
    |--为所有的文件建立索引号 FileID_Number
	  |--首先查找到所有的文件目录 file.list[]
	  |--将所有的文件写到一个文件索引文件中 fileIndex.txt
step2:
    |--根据文件的路径将文件加载到程序中，并将其中的单词分词统计
	  |--统计每个单词在各个文件中出现的频率，并将统计信息写到结果文件wordIndex.txt中

*/

import java.io.*;
import java.util.*;

class InvertedEngine
{
	public static void main(String[] args) throws IOException
	{	
		String filePath = "documents";
		String docIndex = "docIndex.txt";
		String wordIndex = "wordIndex.txt";
		getFileIndex(filePath , docIndex);
        getWordsFrequency(docIndex,wordIndex);
		System.out.println("Work Done!");
	}

	public static void getFileIndex(String filePath , String docIndex)
	{
		//通过传入的filePath找到文件所在，并将该文件下所有文件信息写到docIndex.txt中
		File file = new File(filePath);
		File[] fileList = file.listFiles();	  	
		BufferedWriter bufw = null;
        try 
        {   //将所有filePath下的文件路径写到docIndex文件中
		    bufw = new BufferedWriter(new FileWriter(docIndex));
			for(int x = 0 ; x <fileList.length ; x++ )
			{
				String docPath = fileList[x].getAbsolutePath();			
				bufw.write("DocID_" + x + "\t" + docPath);
				bufw.newLine();bufw.flush();//刷新写入
			}
        }
        catch (IOException e)
        {
			System.out.println("打开文件失败" + e);
        }
	    finally
		{
			try
			{
				if(bufw != null)
					bufw.close();
			}
			catch (IOException ex)
			{
				System.out.println("关闭文件失败" + ex);
			}
		}
	}
	public static void getWordsFrequency(String docIndex , String wordIndex) throws IOException
	{ //通过docIndex文件中的内容找到每个文件，并将文件中的内容做单词统计
       TreeMap<String,TreeMap<String,Integer>>  tmp = new TreeMap<String,TreeMap<String,Integer>>();//统计map
	   BufferedReader bufr = new BufferedReader(new FileReader(docIndex));//读取docIndex.txt
	   BufferedWriter bufw = new BufferedWriter(new FileWriter(wordIndex));//写入到wordIndex.txt
	   BufferedReader bufrDoc = null;
	   String docIDandPath = null;
	   while( (docIDandPath = bufr.readLine()) != null)
		{
		      String[] docInfo = docIDandPath.split("\t");
			  String docID = docInfo[0]; String docPath = docInfo[1];//获取到docID和文件的路径
			  bufrDoc = new BufferedReader(new FileReader(docPath));
			  String  wordLine = null;	 
			  while( (wordLine = bufrDoc.readLine()) != null)
				{
				  String[] words = wordLine.split("\\W");
				  for(String wordOfDoc : words)
					  if(!wordOfDoc.equals(""))
						  wordDeal(wordOfDoc,docID,tmp);//将从docIndex读取到对应文件内容对做统计处理			    		       
				}
		} 
        //将处理后的结果写入到wordIndex.txt文件中		
		String wordFreInfo = null;
		Set<Map.Entry<String,TreeMap<String,Integer>>> entrySet = tmp.entrySet();
		Iterator<Map.Entry<String,TreeMap<String,Integer>>> it = entrySet.iterator();
		while(it.hasNext())
		{
			Map.Entry<String,TreeMap<String,Integer>> em = it.next();
			wordFreInfo = em.getKey() +"\t" + em.getValue();
	        bufw.write(wordFreInfo);
			bufw.newLine();bufw.flush();
		}
		bufw.close();
		bufr.close();
		bufrDoc.close();
	}
	public static void wordDeal(String wordOfDoc,String docID,TreeMap<String,TreeMap<String,Integer>> tmp)
	{
        wordOfDoc = wordOfDoc.toLowerCase();
        if(!tmp.containsKey(wordOfDoc))
		{	
		  //单词在统计中是首次出现	
			TreeMap<String , Integer> tmpST = new TreeMap<String , Integer>();
			tmpST.put(docID,1);
			tmp.put(wordOfDoc,tmpST);
		}        
		else
		{//单词在tmp中已近存在获取该单词在对应docID中出现次数，若是首次出现
		 //count = null，则将（docID ,1)加入到tmpST中；若不是首次出现，则将count++后，再将信息回写到tmpST中。
		 TreeMap<String ,Integer> tmpST = tmp.get(wordOfDoc);
		 Integer count = tmpST.get(docID);
		 count = ((count == null) ? 1 : count++);
		 tmpST.put(docID,count);				
		 tmp.put(wordOfDoc,tmpST);	//将最新结果回写到tmp中	 
		}
	}
}
