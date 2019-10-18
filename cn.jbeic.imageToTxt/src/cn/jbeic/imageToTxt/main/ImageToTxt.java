/**============================================================
 * 版权： 久其软件 版权所有 (c) JOIN-CHEER
 * 包： cn.jbeic.imageToTxt.main
 * 修改记录：
 * 日期                作者           内容
 * =============================================================
 * 2019年10月18日       蔡金贝        
 * ============================================================*/

package cn.jbeic.imageToTxt.main;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.aip.ocr.AipOcr;
/**
 * TODO 简述                 .<br>
 *<p>TODO 详细描述</p>
 *
 * <p>Copyright: 版权所有 (c) JOIN-CHEER</p>
 *
 * @author 蔡金贝
 * @version 2019年10月18日
 */

public class ImageToTxt {
	
	public static final String APP_ID = "11013335";
	public static final String API_KEY = "Ze21tEQq2hbWonWW2KMuGkyF";
	public static final String SECRET_KEY = "BrwunVMnGWSKITgFXljGCDjxbPHiplog";
	

	/**
	 * TODO           .<br>
	 *
	 * @param args  void 
	 */
	public static void main(String[] args) {
		
		try {
			System.out.println(System.getProperty("user.dir"));
			getImageFiles(System.getProperty("user.dir"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * TODO  图片转TXT       .<br>
	 *  void 
	 */
	private static void imageToTxt(String imagePath) {
		try {
			AipOcr ocrclient = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
			ocrclient.setConnectionTimeoutInMillis(3000);
			ocrclient.setSocketTimeoutInMillis(6000);
			HashMap<String, String> options = new HashMap<String, String>();
			options.put("language_type", "ENG");
			options.put("detect_direction", "true");
			options.put("probability", "true");
			JSONObject res = ocrclient.accurateGeneral(imagePath, options);
			if (res.toString().contains("words_result")&&res.getJSONArray("words_result").toList().size() > 0) {
				JSONArray array=res.getJSONArray("words_result");
				FileWriter fileWriter = null;
				try {
					fileWriter = new FileWriter(imagePath+".txt");//创建文本文件
					for (int i = 0; i < array.length(); i++) {
						//System.err.println(array.getJSONObject(i).getString("words").trim());
						fileWriter.write(array.getJSONObject(i).getString("words")+System.getProperty("line.separator") );//写入 \r\n换行
					}
					fileWriter.flush();
					fileWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * TODO    获取要处理的图片数据       .<br>
	 *
	 * @param path
	 * @throws Exception  void 
	 */
	public static void getImageFiles(String path) throws Exception {
		File file = new File(path);
		File[] tempList = file.listFiles();
		for (int i = 0; i < tempList.length; i++) {
			if (tempList[i].isDirectory()) {
				getImageFiles(tempList[i].getAbsolutePath());
			}
			if (tempList[i].getName().endsWith("png")||tempList[i].getName().endsWith("PNG")||tempList[i].getName().endsWith("jpg")||tempList[i].getName().endsWith("JPG")) {
				System.err.println(tempList[i].getParent() + File.separator + tempList[i].getName());
				imageToTxt(tempList[i].getParent() + File.separator + tempList[i].getName());
			}
		}
	}
	

}
