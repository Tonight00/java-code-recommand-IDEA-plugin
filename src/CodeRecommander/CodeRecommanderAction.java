package CodeRecommander;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Collections.sort;

public class CodeRecommanderAction extends AnAction {

    static String downloadlink = "http://www.baidu.com/";
    static String pp = "14";
    static String pyp = "20";
    String packagePath = PropertiesComponent.getInstance().getValue(
            pp);
    String pythonPathi = PropertiesComponent.getInstance().getValue(
            pyp);
    String filePath = packagePath + "/Main1.py";
    String[] pythonData = new String[]{"/Library/Frameworks/Python.framework/Versions/3.6/bin/python3", filePath,  packagePath};
    Boolean iscomplie = false;
    Process pr = new Process() {
        @Override
        public OutputStream getOutputStream() {
            return null;
        }

        @Override
        public InputStream getInputStream() {
            return null;
        }

        @Override
        public InputStream getErrorStream() {
            return null;
        }

        @Override
        public int waitFor() throws InterruptedException {
            return 0;
        }

        @Override
        public int exitValue() {
            return 0;
        }

        @Override
        public void destroy() {

        }
    };
    String way = "";
    static String writeline = "";

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        way += "6";
        //Messages.showInfoMessage(way,"title");
        try {
            JBPopupFactory factory = JBPopupFactory.getInstance();
            JBPopup factoryMessage = factory.createMessage("The recommended " +
                    "code will be automatically write on your file");
            factoryMessage.showInBestPositionFor(e.getDataContext());
            Editor editor = e.getData(CommonDataKeys.EDITOR);
            Project project = e.getData(CommonDataKeys.PROJECT);
            int offset = editor.getCaretModel().getOffset();
            File file =
                    new File(PropertiesComponent.getInstance().getValue(pp,
                            ""));
            //Messages.showInfoMessage("after try","title");
            if (!file.exists()) {
                Messages.showInfoMessage("Please turn to the setting page",
                        "title");
            }
            //Messages.showInfoMessage("after init","title");
            String packagePath = PropertiesComponent.getInstance().getValue(
                    pp);
            //String filePath = PropertiesComponent.getInstance().getValue(fp);
            String filePath = packagePath + "/Main1.py";
            //Messages.showInfoMessage(filePath,"title");
            String filetxt = e.getData(PlatformDataKeys.FILE_TEXT);
            //Messages.showInfoMessage("py","title");
            filetxt = filetxt.substring(0,offset)+"\n###\n";
            //Messages.showInfoMessage(filePath,"filepath");
            String[] pythonData = new String[]{"/Library/Frameworks/Python.framework/Versions/3.6/bin/python3", filePath, filetxt
                    , packagePath};
            //Process pr = Runtime.getRuntime().exec(pythonData);
            //Messages.showInfoMessage("afterpy","title");
            InputStreamReader ir = new InputStreamReader(pr.getInputStream());
            LineNumberReader in = new LineNumberReader(ir);
            OutputStream out = pr.getOutputStream();
            //Messages.showInfoMessage(filetxt,"t");
            //Messages.showInfoMessage(way,"title");
            String[] ftsplit = filetxt.split("\n");
            /*
            for (int i=0;i <ftsplit.length;i++){
                out.write(ftsplit[i].getBytes());
                out.flush();
            }
            */
            out.write(filetxt.getBytes());
            out.flush();
            String line1;
            String line2 = "";
            String line3 = "";

            //in.readLine();
            //in.readLine();
            String line0 = in.readLine();
            //Messages.showInfoMessage(line0,"input");
            way += "7";
            while ((line1 = in.readLine()) != null) {
                line2 = line2.concat(line1 + "\n");
                line1 = in.readLine();
                line3 = line3.concat(line1 + "\n");
                way += "8";
            }
            String[] optionstring = line2.split("\n");
            String[] scorestring = line3.split("\n");
            ir.close();
            in.close();
            pr.waitFor();
            way += "9";
            precomplie();
            iscomplie=true;
            way += " 10";
            //Messages.showInfoMessage(way,"title");
            DefaultActionGroup actionGroup =
                    (DefaultActionGroup) ActionManager.getInstance().getAction("CodeRecommender");
            actionGroup.removeAll();
            for (int i = 0;i < 10;i++){
                String opstring = optionstring[i];
                actionGroup.add(new AnAction((scorestring[i] + optionstring[i]).replace("$$","var")) {
                    @Override
                    public void actionPerformed(AnActionEvent e) {
                        addcodeline(editor,project,opstring);
                        writeline=opstring;
                        buildgroup(e);
                    }
                });
            }
            ListPopup listPopup =
                    JBPopupFactory.getInstance().createActionGroupPopup(
                            "CodeRecommender",
                            actionGroup, e.getDataContext(), JBPopupFactory.ActionSelectionAid.SPEEDSEARCH, false);
            listPopup.showInBestPositionFor(e.getDataContext());
            //Messages.showInfoMessage(line0,"t");
        } catch (Exception f) {
            way += "7";
            f.printStackTrace();
            Messages.showInfoMessage("wrong","t");
            Messages.showInfoMessage(way,"title");

        }
    }

    @Override
    public void update(AnActionEvent e) {
        way += "1";
        if (iscomplie==false) {
            way += "2";
            precomplie();
        }
    }

    public void precomplie() {
        try {
            way += "4";
            pr = Runtime.getRuntime().exec(pythonData);
            iscomplie=true;
        } catch (Exception f) {
            way += "5";
            f.printStackTrace();
        }
    }

    public static String findpath() {
        JFileChooser chooser = new JFileChooser();
        /*
         * 根据JFileChooser对弹出的文件夹框选择 1、只选择目录JFileChooser.DIRECTORIES_ONLY
         * 2、只选择文件JFileChooser.FILES_ONLY
         * 3、目录或者文件都可以JFileChooser.FILES_AND_DIRECTORIES
         */
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

// 保存所选目录chooser.showSaveDialog(parent);
        Component parent = null;
        int returnVal = chooser.showSaveDialog(parent);
        String selectPath = "";



// 获得选中的文件对象JFileChooser.APPROVE_OPTION
// 如果保存的目录跟获得选中的文件对象一致，成功都是返回0
        if (returnVal == JFileChooser.APPROVE_OPTION) {


// 获得路径
            selectPath = chooser.getSelectedFile().getPath();
            System.out.println("你选择的目录是：" + selectPath);
            return selectPath;
        }
        System.exit(0);
        return selectPath;
    }


    public void addcodeline(Editor editor, Project project, String str){
        //Access document, caret, and selection
        final Document document = editor.getDocument();
        final SelectionModel selectionModel = editor.getSelectionModel();
        CaretModel caretModel = editor.getCaretModel();

        final int start = selectionModel.getSelectionStart();
        int index = selectionModel.getSelectionStart();
        WriteCommandAction.runWriteCommandAction(project,() ->
                document.insertString(start,str));
        int offset = caretModel.getOffset();
        String text = editor.getDocument().getText();
        int flag = 0,i=0;
        for ( i =0;i<text.length();i++){
            if(text.charAt(i)=='$') {flag = 1;break;}
        }
       // caretModel.moveToOffset(offset+str.length());
        if(flag == 1)caretModel.moveToOffset(i);

    }

    public static void download(String url){
        try {
            URI uri = URI.create(url);
            // getDesktop()返回当前浏览器上下文的 Desktop 实例。
            //Desktop 类允许 Java 应用程序启动已在本机桌面上注册的关联应用程序，以处理 URI 或文件。
            Desktop dp = Desktop.getDesktop();
            //判断系统桌面是否支持要执行的功能
            if (dp.isSupported(Desktop.Action.BROWSE)) {
                //启动默认浏览器来显示 URI
                dp.browse(uri);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setpath(){
        String selectedpackagepath = findpath();
        PropertiesComponent.getInstance().setValue(pp, selectedpackagepath);
    }

    public static void addcode (Editor editor, Project project, String str) {

        final Document document = editor.getDocument();
        String text = document.getText();
        final SelectionModel selectionModel = editor.getSelectionModel();
        CaretModel caretModel = editor.getCaretModel();
        final int start = selectionModel.getSelectionStart();
        int n = text.length(),flag =0,i =0;
        for(i =0 ; i<n;i++)
            if(text.charAt(i) == '$') {
                flag = 1;
                break;
            }
        final int pos = i;
        if(flag ==0) return ;
        caretModel.moveToOffset(i);

        WriteCommandAction.runWriteCommandAction(project, () ->
                document.replaceString(pos,pos+2,str));
    }





    public static void buildgroup (AnActionEvent e) {
        try {
            //Messages.showInfoMessage("1st","t");
            Editor editor = e.getData(CommonDataKeys.EDITOR);
            Project project = e.getData(CommonDataKeys.PROJECT);

            DefaultActionGroup actionGroup =
                    (DefaultActionGroup)
                            ActionManager.getInstance().getAction("CodeRecommender");
            actionGroup.removeAll();
            String text = editor.getDocument().getText();
            ArrayList<String> actions =getGroupnumber(text);
            int i= 0,n = text.length(),flag = 0;
            for(i =0 ; i<n;i++)
                if(text.charAt(i) == '$') {
                    flag = 1;
                    break;
                }

            if(flag == 0) {
                return ;
            }
            for (i =0 ;i<actions.size();i++) {
                String s = actions.get(i);
                actionGroup.add(new AnAction(actions.get(i)) {
                    @Override
                    public void actionPerformed (AnActionEvent hel) {
                        addcode(editor,project,s);
                        buildgroup(hel);
                    }
                });
            }
            ListPopup listPopup =
                    JBPopupFactory.getInstance().createActionGroupPopup(
                            "Code ReCommender",
                            actionGroup, e.getDataContext(),
                            JBPopupFactory.ActionSelectionAid.SPEEDSEARCH, false);
            listPopup.showInBestPositionFor(editor);
            //JBPopupFactory.getInstance().createPopupChooserBuilder()
        } catch (Exception f) {
            f.printStackTrace();
        }



    }
    public static ArrayList<String> getGroupnumber( String text){
        ArrayList<String>value = getVarity(text);
        return value;
    }
    public static  ArrayList<String> getVarity(String text) {

        String [] lines = text.split("\n");
        String [] kwd = {"int","char","boolean","double","String","long",
                "double","float","BigInteger"};
        ArrayList<String> keywords = new ArrayList<>();
        ArrayList<String> DIY = new ArrayList<>();
        ArrayList<String> basicVarity = new ArrayList<>();
        HashMap<String,Integer> map = new HashMap<String, Integer>();

        for (int i =0 ;i<lines.length;i++) {
            String [] words = lines[i].split("\\s+");
            for (int k =0;k<words.length;k++){
                if (words[k].equals("class") ){
                    String x =readvalue(words[k+1]);
                    if(x.equals("") == false) DIY.add(words[k+1]);
                    break; }
            } }

        for (int i =0 ;i<DIY.size();i++) {
            keywords.add(DIY.get(i));
        }
        for (int i =0;i<kwd.length;i++) {
            keywords.add(kwd[i]);
        }

        int flag = 0;
        for (int i = 0; i<keywords.size();i++)
            for (int j =0;j<lines.length;j++) {
                String [] words = lines[j].split("\\s+");
                for (int k =0;k<words.length;k++) {
                    if(words[k].equals(keywords.get(i)) ) {
                        k++;int cnt =0;
                        while (k<words.length && cnt <5) {
                            if(isVarity(words[k])) {
                                String x = readvalue(words[k]);
                                if(x.equals("")) break;
                                if(!basicVarity.contains(x) )
                                    basicVarity.add(x);
                                map.put(x,j);
                                flag = 1;break;
                            }
                            cnt++;k++;
                        }
                        if(flag == 1) {break;}
                    }
                }
                flag = 0;
            }


        String [] words =text.split(" ");
        ArrayList<Varity> varity = new ArrayList<>();
        for (int i =0 ;i<basicVarity.size();i++) {
            int cnt = 0;
            for (int j =0 ;j<words.length;j++) {
                String x  = readvalue(words[j]);
                if(x.equals(basicVarity.get(i))) cnt++;
            }
            String x = basicVarity.get(i);
            varity.add(new Varity(x,cnt,map.get(x)));
        }
        //给varity封装好变量、距离、频率。
        sort(varity);
        ArrayList<String > ans  = new ArrayList<>();
        for (int i = 0; i<varity.size();i++) {
            System.out.print(varity.get(i).body + "  ");
            System.out.println(varity.get(i).times+ " " +varity.get(i).distance);
            ans.add(varity.get(i).body);
        }
        // 输出基本变量的比较


        String mpn = "\\s*+\\.\\s*+[0-9a-zA-Z_]*+";
        ArrayList<String > method = new ArrayList<>();
        for(int i =0; i<basicVarity.size();i++) {
            int r = 0;
            String pattern = basicVarity.get(i)+ mpn;
            Matcher m = getMatcher(text,pattern);
            while(m.find(r)){
                String str = m.group(0);
                str = str.replace(" \t","");
                if(!method.contains(str)) method.add(str);
                r = m.end();
            }
        }
        ArrayList<String> demo = new ArrayList<>();
        String []writeli =writeline.split(" ");
        if (writeli[0].equals("for")) {
            demo.add("i");demo.add("j");
            demo.add("0");
        }
        for (int i =0 ;  i<ans.size();  i++) {
            if(!demo.contains(ans.get(i))) demo.add(ans.get(i));
        }
        for(int i =0; i<3 && i<method.size();i++) {
            demo.add(method.get(i));
        }



        return demo;
    }
    public static String readvalue(String word) {
        String value = "";
        if(word.length() == 0) return "";
        char c = word.charAt(0);
        int i =0;
        while( Character.isLetter(c) || Character.isDigit(c) || c == '_') {
            value+=c;i++;
            if(i<word.length()) c = word.charAt(i);
            else break;
        }
        if(i<word.length()) {
            String x = "\\([<>a-zA-Z0-9\\._]*+[\\)]*+";
            Matcher m = getMatcher(word,x);
            if(m.find(i)) return "";
        }
        if(value.equals("implements")||value.equals("extends")||value.equals("interface")
                ||value.equals("abstract")) return "";
        return value;
    }

    private static Matcher getMatcher(String line, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(line);
        return m;
    }
    public static boolean isVarity(String word) {
        char c = word.charAt(0);
        if(word.charAt(0)!='_' &&

                Character.isLetter(c) == false) {
            return false;
        }
        return true;
    }
}

class  Varity implements Comparable {
    public  int times;
    public  int  distance;
    public  String body;
    Varity(String body,int times,int distance) {
        this.body = body;
        this.times = times;
        this.distance =distance;
    }
    @Override
    public int compareTo(Object o) {
        Varity a = (Varity) o;
        long  suma = a.distance*a.times;
        long  sum   = distance*times;
        if(sum < suma)  return 1;
        else if (sum == suma) return 0;
        else  return -1;
    }
}