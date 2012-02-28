package kt0319;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.OutputType;

import com.opera.core.systems.OperaDriver;

public class WebLinkCheck extends Thread {

	private Boolean end_flag = false;

	private String baseUrl;
	private String baseDir;
	private String basePage;
	private String domain;
	private String picdir;
	private String ngword;
	private String picdatedir;

	private static int limit_link = 0;
	private static int limit_get = 0;
	private static int limit_level = 0;
	private static int limit_recursive = 0;
	private static int tmp_recursive=0;

	private WebDriver driver;
	private HashMap<String, ArrayList<String>> LinkLists = new HashMap<String, ArrayList<String>>();
	private ArrayList<String> CheckedLinkList = new ArrayList<String>();

	private HashMap<String, ArrayList<String>> getParam = new HashMap<String, ArrayList<String>>();

	private static int iterator_num = 2;
	MainFrame mf;

	public void start(MainFrame mainframe) {
		mf = mainframe;
		setBrowser_type("firefox");
		setBaseUrl(mf.URL_textfield.getText());
		setNgword(mf.NG_textField.getText());
		setPicdir(mf.PIC_textField.getText());
		setLimit_link(Integer.parseInt(mf.limit_link_textField.getText()));
		setLimit_get(Integer.parseInt(mf.limit_get_textField.getText()));
		setLimit_level(Integer.parseInt(mf.limit_level_textField.getText()));
		setLimit_recursive(Integer.parseInt(mf.limit_recursive_textField.getText()));
		end_flag = false;
		iterator_num = 2;
		create_dir();
		super.start();
	}

	public void run() {
		mf.result_update("ブラウザを起動中\n");
		setDriver();
		/*
		 * mf.result_update("設定:"); if(limit_link>0){
		 * mf.result_update(" リンク制限 "); } if(limit_recursive>0){
		 * mf.result_update(" 再帰制限 "); } if(limit_get>0){
		 * mf.result_update(" GET文字列制限 "); } if(limit_level>0){
		 * mf.result_update(" 階層制限 "); }
		 */
		mf.csv_update(movelink(baseUrl, ""));
		recursive_check(baseUrl, "",0);
		driver.close();
		mf.csvSave(cnvfilename(baseUrl));
		mf.result_update("CSVを保存しました。正常に終了しました\n");
		mf.stopcheck();
	}

	public void recursive_check(String url, String mark,int recursive_num) {
		int check_num = 0;
		mf.result_update(mark + "(" + url + ")のリンクを取得\n");
		/*
		mf.result_update("基本URL:" + baseUrl + "");
		mf.result_update("基本ページ:" + basePage + "");
		mf.result_update("基本ディレクトリ:" + baseDir + "");
		mf.result_update("ドメイン名:" + domain + "");
		*/
		mf.result_update("\n");
		HashMap<String, ArrayList<String>> linkhash = createLinkList(driver.getPageSource(), mark);
		//mf.result_update("再帰制限数数:"+String.valueOf(limit_recursive)+"再帰数:"+String.valueOf(recursive_num)+"\n");
		mf.result_update("総リンク数:" + linkhash.get("All"+mark).size() + " 取得リンク数:" + linkhash.get("Check"+mark).size() + " ");
		mf.result_update("取得済みリンク数:" + CheckedLinkList.size() + " 外部ドメインリンク数:" + linkhash.get("NgDomain"+mark).size() + " NGリンク数:" + linkhash.get("Ng"+mark).size() + "\n");
		tmp_recursive=0;
		while (end_check(linkhash.get("Check"+mark).size(),check_num)) {
			String next_link=linkhash.get("Check"+mark).get(check_num);
			String check_mark = mark +"_"+String.valueOf(check_num+1);

			Pattern p = Pattern.compile("^_");
			Matcher m = p.matcher(check_mark);
			check_mark = m.replaceAll("");
			// 次のリンクへ 再帰で表示しているため多重になる
			mf.csv_update(movelink(next_link, check_mark));
			// 再帰処理
			if (recursive_num < limit_recursive) {
				recursive_check(next_link, check_mark,recursive_num+1);
			}
			iterator_num++;
			check_num += 1;
		}
	}

	public void interrupt() {
		super.interrupt();
	}

	public void setBrowser_type(String browser_type) {
	}

	public void setPicdir(String picdir) {
		this.picdir = picdir;
	}

	public void setNgword(String ngword) {
		this.ngword = ngword;
	}

	public Boolean getEnd_flag() {
		return end_flag;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
		String regex = "http://([^/]*)/";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(baseUrl);
		if (m.find()) {
			domain = m.group(1);
		}

		String regex3 = "^(http://[^/]*/([^/]*/)*)(.*)$";
		Pattern p3 = Pattern.compile(regex3);
		Matcher m3 = p3.matcher(baseUrl);
		if (m3.find()) {
			baseDir = m3.group(1);
			basePage = m3.group(3);
		}
	}

	public void setLimit_link(int limit_num_link) {
		this.limit_link = limit_num_link;
	}

	public void setLimit_get(int limit_num_get) {
		this.limit_get = limit_num_get;
	}

	public void setLimit_level(int limit_num_lebel) {
		this.limit_level = limit_num_lebel;
	}

	public int getLimit_recursive() {
		return limit_recursive;
	}

	public void setLimit_recursive(int limit_recursive) {
		this.limit_recursive = limit_recursive;
	}

	public void setDriver() {
		if (mf.rdbtnFirefox.isSelected()) {
			this.driver = new FirefoxDriver();
		} else if (mf.rdbtnIe.isSelected()) {
			this.driver = new InternetExplorerDriver();
		} else if (mf.rdbtnChrome.isSelected()) {
			System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
			this.driver = new ChromeDriver();
		} else if (mf.rdbtnOpera.isSelected()) {
			this.driver = new OperaDriver();
		}
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public String cnvfilename(String url) {
		String filename = url.replace("http://", "");
		filename = filename.replaceAll("[/\\.:*\"<>|?&;]", "_");
		if (filename.length() > 120) {
			filename = filename.substring(0, 50) + "_#_#_" + filename.substring(filename.length() - 50, filename.length());
		}
		return filename;
	}
	public String getdatetime(){
		Calendar cal1 = Calendar.getInstance();
		String mon = String.format("%02d", cal1.get(Calendar.MONTH) + 1);
		String date = String.format("%02d", cal1.get(Calendar.DATE));
		String hh = String.format("%02d", cal1.get(Calendar.HOUR));
		String mm = String.format("%02d", cal1.get(Calendar.MINUTE));
		String ss = String.format("%02d", cal1.get(Calendar.SECOND));
		return mon + date + hh + mm +ss;
	}
	public void create_dir(){

		picdatedir = picdir + "\\" + getdatetime() + "_" + cnvfilename(baseDir);
		File newdir = new File(picdatedir);
		newdir.mkdir();
	}

	public String movelink(String link, String mark) {
		String filename = picdatedir + "\\" + mark + cnvfilename(link) + ".png";
		this.driver.get(link);
		saveScreenshot(new File(filename));
		return getdatetime() + "," +filename + ","+ this.driver.getTitle() +","+link + "\n";
	}

	private HashMap<String, ArrayList<String>> createLinkList(String source, String mark) {
		HashMap<String, ArrayList<String>> LinkHash = new HashMap<String, ArrayList<String>>();
		String regex = "<a href=\"([^\"]*)\">";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(source);

		ArrayList<String> AllLinkList = new ArrayList<String>();
		ArrayList<String> CheckLinkList = new ArrayList<String>();
		ArrayList<String> NgLinkList = new ArrayList<String>();
		ArrayList<String> NgDomainLinkList = new ArrayList<String>();

		while (m.find()) {
			AllLinkList.add(m.group(1));
		}

		for (int i = 0; i < AllLinkList.size(); i++) {
			String url = AllLinkList.get(i);
			url = url.replaceAll("&amp;", "&");

			if (check_linkurl(url)) {
				Pattern p1 = Pattern.compile("/" + ngword + "/");
				Matcher m1 = p1.matcher(url);

				String reg2 = "^http://";
				Pattern p2 = Pattern.compile(reg2);
				Matcher m2 = p2.matcher(url);

				String reg3 = "^http://" + baseDir;
				Pattern p3 = Pattern.compile(reg3);
				Matcher m3 = p3.matcher(url);

				if (m1.find() && !ngword.equals("")) {
					NgLinkList.add(url);
				} else if (m3.find()) {
					CheckLinkList.add(url);
				} else if (!m2.find()) {
					url = baseDir + url;
					CheckLinkList.add(url);
				} else {
					NgDomainLinkList.add(url);
				}
				driver.getPageSource();
			}
		}
		LinkHash.put("All"+mark, AllLinkList);
		LinkHash.put("Check"+mark, CheckLinkList);
		LinkHash.put("NgDomain"+mark, NgDomainLinkList);
		LinkHash.put("Ng"+mark, NgLinkList);
		return LinkHash;
	}

	private boolean check_linkurl(String url) {
		String regex = "^mailto:";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(url);
		if (m.find()) {
			return false;
		}
		return true;
	}

	private void saveScreenshot(File saveFile) {
		try {
			if (this.driver instanceof TakesScreenshot) {
				File tmpFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
				FileHandler.copy(tmpFile, saveFile);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public boolean end_check(int size,int check_num) {
		if (check_num >= size) {
			return false;
		}
		if (iterator_num >= this.limit_link) {
			mf.result_update("リンク制限数を超えたため終了\n");
			return false;
		}
		return true;
	}

}
