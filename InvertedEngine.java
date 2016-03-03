/*
Author:��Դ
2013-03-24

����Ϊ�ļ�������������
step1��
    |--Ϊ���е��ļ����������� FileID_Number
	  |--���Ȳ��ҵ����е��ļ�Ŀ¼ file.list[]
	  |--�����е��ļ�д��һ���ļ������ļ��� fileIndex.txt
step2:
    |--�����ļ���·�����ļ����ص������У��������еĵ��ʷִ�ͳ��
	  |--ͳ��ÿ�������ڸ����ļ��г��ֵ�Ƶ�ʣ�����ͳ����Ϣд������ļ�wordIndex.txt��

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
		//ͨ�������filePath�ҵ��ļ����ڣ��������ļ��������ļ���Ϣд��docIndex.txt��
		File file = new File(filePath);
		File[] fileList = file.listFiles();	  	
		BufferedWriter bufw = null;
        try 
        {   //������filePath�µ��ļ�·��д��docIndex�ļ���
		    bufw = new BufferedWriter(new FileWriter(docIndex));
			for(int x = 0 ; x <fileList.length ; x++ )
			{
				String docPath = fileList[x].getAbsolutePath();			
				bufw.write("DocID_" + x + "\t" + docPath);
				bufw.newLine();bufw.flush();//ˢ��д��
			}
        }
        catch (IOException e)
        {
			System.out.println("���ļ�ʧ��" + e);
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
				System.out.println("�ر��ļ�ʧ��" + ex);
			}
		}
	}
	public static void getWordsFrequency(String docIndex , String wordIndex) throws IOException
	{ //ͨ��docIndex�ļ��е������ҵ�ÿ���ļ��������ļ��е�����������ͳ��
       TreeMap<String,TreeMap<String,Integer>>  tmp = new TreeMap<String,TreeMap<String,Integer>>();//ͳ��map
	   BufferedReader bufr = new BufferedReader(new FileReader(docIndex));//��ȡdocIndex.txt
	   BufferedWriter bufw = new BufferedWriter(new FileWriter(wordIndex));//д�뵽wordIndex.txt
	   BufferedReader bufrDoc = null;
	   String docIDandPath = null;
	   while( (docIDandPath = bufr.readLine()) != null)
		{
		      String[] docInfo = docIDandPath.split("\t");
			  String docID = docInfo[0]; String docPath = docInfo[1];//��ȡ��docID���ļ���·��
			  bufrDoc = new BufferedReader(new FileReader(docPath));
			  String  wordLine = null;	 
			  while( (wordLine = bufrDoc.readLine()) != null)
				{
				  String[] words = wordLine.split("\\W");
				  for(String wordOfDoc : words)
					  if(!wordOfDoc.equals(""))
						  wordDeal(wordOfDoc,docID,tmp);//����docIndex��ȡ����Ӧ�ļ����ݶ���ͳ�ƴ���			    		       
				}
		} 
        //�������Ľ��д�뵽wordIndex.txt�ļ���		
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
		  //������ͳ�������״γ���	
			TreeMap<String , Integer> tmpST = new TreeMap<String , Integer>();
			tmpST.put(docID,1);
			tmp.put(wordOfDoc,tmpST);
		}        
		else
		{//������tmp���ѽ����ڻ�ȡ�õ����ڶ�ӦdocID�г��ִ����������״γ���
		 //count = null���򽫣�docID ,1)���뵽tmpST�У��������״γ��֣���count++���ٽ���Ϣ��д��tmpST�С�
		 TreeMap<String ,Integer> tmpST = tmp.get(wordOfDoc);
		 Integer count = tmpST.get(docID);
		 count = ((count == null) ? 1 : count++);
		 tmpST.put(docID,count);				
		 tmp.put(wordOfDoc,tmpST);	//�����½����д��tmp��	 
		}
	}
}
