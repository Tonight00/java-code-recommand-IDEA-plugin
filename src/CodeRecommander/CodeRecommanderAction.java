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
import org.jetbrains.annotations.NotNull;

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
    String packagePath = "/Volumes/文档/大学/大学学习/大二下/PythonSupporting";
    //String packagePath = PropertiesComponent.getInstance().getValue(pp);
    String pythonPathi = PropertiesComponent.getInstance().getValue(
            pyp);
    String filePath = packagePath + "/Main.py";
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
            if (!file.exists()) {
                Messages.showInfoMessage("Please turn to the setting page",
                        "title");
            }
            String packagePath = PropertiesComponent.getInstance().getValue(
                    pp);
            String filePath = packagePath + "/Main1.py";
            String filetxt = e.getData(PlatformDataKeys.FILE_TEXT);
            filetxt = filetxt.substring(0,offset)+"\n###\n";
            String[] pythonData = new String[]{"/Library/Frameworks/Python.framework/Versions/3.6/bin/python3", filePath, filetxt
                    , packagePath};
            InputStreamReader ir = new InputStreamReader(pr.getInputStream());
            LineNumberReader in = new LineNumberReader(ir);
            OutputStream out = pr.getOutputStream();
            out.write(filetxt.getBytes());
            out.flush();
            String line1;
            String line2 = "";
            String line3 = "";

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
            DefaultActionGroup actionGroup =
                    (DefaultActionGroup) ActionManager.getInstance().getAction("CodeRecommender");
            actionGroup.removeAll();
            for (int i = 0;i < 10;i++){
                String opstring = optionstring[i];
                actionGroup.add(new AnAction((scorestring[i] + rmsymb(optionstring[i]))) {
                    @Override
                    public void actionPerformed(AnActionEvent e) {
                        addcodeline(editor,project,opstring);
                        //writeline=opstring;
                        String the_line = opstring;
                        BuilThreeAction(e, the_line);
                        //buildgroup(e);
                    }
                });
            }
            ListPopup listPopup =
                    JBPopupFactory.getInstance().createActionGroupPopup(
                            "CodeRecommender",
                            actionGroup, e.getDataContext(), JBPopupFactory.ActionSelectionAid.SPEEDSEARCH, false);
            listPopup.showInBestPositionFor(e.getDataContext());
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

    public static String rmsymb (String rawstr) {

        return rawstr.replace("$$$","$Var").replace("!!!","$Const").replace(
                "###","$Type").replace("~~~","$Method()");
        //return rawstr;
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


    public static void setpath(){
        String selectedpackagepath = findpath();
        PropertiesComponent.getInstance().setValue(pp, selectedpackagepath);
    }
    public void BuilThreeAction (AnActionEvent e, String the_line)
    {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        Project project = e.getData(CommonDataKeys.PROJECT);
        DefaultActionGroup actionGroup = (DefaultActionGroup) ActionManager.getInstance().getAction("CodeRecommender");
        String text = editor.getDocument().getText();
        actionGroup.removeAll();

        //FirstAction
        actionGroup.add(new AnAction("Recommend")
        {
            public void actionPerformed (AnActionEvent e)
            {
                if(MoveCaretToFlag(e)!=-1)
                    BuildRecommendGroup(e, the_line);

            }
        });

        //SecondAction

        //ThirdAction
        actionGroup.add(new AnAction("Input")
        {
            public void actionPerformed (AnActionEvent e)
            {
                Editor editor = e.getData(CommonDataKeys.EDITOR);
                Project project = e.getData(CommonDataKeys.PROJECT);
                String s = Messages.showInputDialog(null, null, null, null, null);
                addcode(editor, project, s);
                if(MoveCaretToFlag(e)!=-1)
                    BuilThreeAction(e, the_line);
            }
        });

        ListPopup listPopup = JBPopupFactory.getInstance().createActionGroupPopup("The Three Musketeers", actionGroup, e.getDataContext(), JBPopupFactory.ActionSelectionAid.SPEEDSEARCH, false);
        listPopup.showInBestPositionFor(editor);

    }


    public void addcode (Editor editor, Project project, String str)
    {

        final Document document = editor.getDocument();
        final SelectionModel selectionModel = editor.getSelectionModel();
        final int start = selectionModel.getSelectionStart();
        CaretModel caretModel = editor.getCaretModel();
        String text = document.getText();
        int n = text.length(), flag = 0, i = serch(text);
        if (i != -1) caretModel.moveToOffset(i);
        else return;
        int pos = i;
        WriteCommandAction.runWriteCommandAction(project, () -> document.replaceString(pos, pos + 3, str));
        caretModel.moveToOffset(pos+str.length());
    }

    public static int serch (String s)
    {
        char[] a = new char[]{'$', '#', '~', '!'};
        for (int i = 0; i < s.length()-2; i++)
            for (int j = 0; j < 4; j++)
            {
                if (s.charAt(i) == a[j] &&s.charAt(i+1) == a[j] && s.charAt(i+2) == a[j] ) return i;
            }
        return -1;
    }
    public static int serch2 (String s)
    {
        char[] a = new char[]{'$', '#', '~', '!'};
        for (int i = 0; i < s.length()-2; i++)
            for (int j = 0; j < 4; j++)
            {
                if (s.charAt(i) == a[j] && s.charAt(i+1) == a[j] && s.charAt(i+2) == a[j]) return j;
            }
        return -1;
    }

    public static int  MoveCaretToFlag (AnActionEvent e)
    {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        Project project = e.getData(CommonDataKeys.PROJECT);
        Document document = editor.getDocument();
        CaretModel caretModel = editor.getCaretModel();
        String text = document.getText();
        int flag = 0, i = serch(text);
        if(i == -1 ) return -1;
        caretModel.moveToOffset(i);
        return 1;
    }


    public void BuildRecommendGroup (AnActionEvent e, String the_line)
    {
        try
        {
            Editor editor = e.getData(CommonDataKeys.EDITOR);
            Project project = e.getData(CommonDataKeys.PROJECT);

            DefaultActionGroup actionGroup = (DefaultActionGroup) ActionManager.getInstance().getAction("CodeRecommender");
            actionGroup.removeAll();
            String text = editor.getDocument().getText();

            String mdoel = "";

            ArrayList<String> actions = merge(text, the_line);
            if (serch(text) == -1) return;
            actionGroup.add(new AnAction("Input")
            {
                public void actionPerformed (AnActionEvent e)
                {
                    Editor editor = e.getData(CommonDataKeys.EDITOR);
                    Project project = e.getData(CommonDataKeys.PROJECT);
                    String s = Messages.showInputDialog(null, null, null, null, null);
                    addcode(editor, project, s);
                    if(MoveCaretToFlag(e)!=-1)
                        BuilThreeAction(e, the_line);
                }
            });

            for (int i = 0; i < actions.size(); i++)
            {
                String s = actions.get(i);

                actionGroup.add(new AnAction(actions.get(i))
                {
                    public void actionPerformed (AnActionEvent e)
                    {
                        addcode(editor, project, s);
                        if(MoveCaretToFlag(e)!=-1)
                            BuilThreeAction(e, the_line);
                    }
                });
            }

            ListPopup listPopup = JBPopupFactory.getInstance().createActionGroupPopup("Code ReCommender", actionGroup, e.getDataContext(), JBPopupFactory.ActionSelectionAid.SPEEDSEARCH, false);
            listPopup.showInBestPositionFor(editor);


        } catch (Exception f)
        {
            f.printStackTrace();
        }


    }

    public static ArrayList<String> merge (String text, String the_line) throws FileNotFoundException
    {

        String[] kwd = {
                "int", "char", "boolean", "double", "long", "double", "float"
        };
        String[] kwd2 = {
                "BigInteger", "String", "Integer", "Double", "BigInteger<>", "HashMap<>", "Set<>", "ArrayList<>"
        };
        ArrayList<String> Varity = getVarity(text, kwd, 0);
        ArrayList<String> Objact = getVarity(text, kwd2, 1);
        ArrayList<String> Method = getMethod(text, Objact);
        ArrayList<String> type = getType(text,kwd, kwd2);

        String path = "/Volumes/文档/大学/大学学习/大二下/PythonSupporting/PythonSupportingPackage/keyofpattern3.txt";
        File myFile = new File(path);
        if(!myFile.exists()) {Messages.showInfoMessage("oo","xx");}
        try
        {
            BufferedReader readerx = new BufferedReader(new FileReader(myFile));
        }
        catch(Exception e){
            String s = e.getStackTrace().toString();
        }
        BufferedReader reader = new BufferedReader(new FileReader(myFile));
        ArrayList<String> ans = new ArrayList<>();
        String tempString = null;
        String[] fourpatterns = new String[5];
        try
        {
            int flag = 0,count = 50;
            while ((tempString = reader.readLine()) != null)
            {
                String sline = tempString + "\n";
                if (sline.contains(the_line))
                {
                    for (int i = 0; i <= 3; i++)
                    {
                        if ((tempString = reader.readLine()) != null) fourpatterns[i] = tempString;
                        else
                        {
                            flag = 1;
                            break;
                        }
                    }
                    if (flag == 1)
                    {
                        break;
                    }
                }
            }
        } catch (IOException e)
        {
            ;
        }
        // ******************************  this is a test.
        int model = serch2(text);
        if (fourpatterns[model].length() > 2)
        {
            addhighword(Varity, Objact, Method, fourpatterns, type, model, ans);
        } else
        {
            if (model == 0)
            {
                for (int k = 0; k < Varity.size(); k++) ans.add(Varity.get(k));
                for (int k = 0; k < Objact.size(); k++) ans.add(Objact.get(k));
            }
            if (model == 1) for (int k = 0; k < type.size(); k++) ans.add(type.get(k));
            if (model == 2) for (int k = 0; k < Method.size(); k++) ans.add(Method.get(k));
        }

        //System.out.println("this is a patternal varity: " + fourpatterns[i]);

        /*
        0:变量
        1:类别
        2:方法
        3:常量
        ～～～方法
        ###类（基础类型和对象类型）
        &&&变量
        ！！！常数（数字，字符串，字符）

        //textpattern是keyofpattern表,text是现有文本。
        */
        return ans;
    }

    public static void addhighword (ArrayList<String> Varity, ArrayList<String> Objact, ArrayList<String> Method, String[] fourpatterns, ArrayList<String> type, int i, ArrayList<String> ans)
    {
        String[] ppwords = fourpatterns[i].split(" ");
        ArrayList<String> pwords = new ArrayList<>();
        for (int j = 0; j < 3 && j < ppwords.length; j++)
        {
            String[] pair = ppwords[j].split(",");
            if (j == 0) pair[0] = pair[0].substring(2);
            pwords.add(pair[0]);
            //System.out.println(words[j]);
        }
        /*
        0:变量
        1:类别
        2:方法
        3:常量
        */
        ArrayList<String> words = new ArrayList<>();
        if (i == 0)
        {
            int size = pwords.size();
            for (int j = 0, k = 0; j < size; j++)
            {
                String s = pwords.get(j);
                for (k = 0; k < Varity.size(); k++) if (s.equals(Varity.get(k))) break;
                if (k == Varity.size())
                {
                    for (k = 0; k < Objact.size(); k++) if (s.equals(Objact.get(k))) break;
                    if (k == Objact.size()) words.add(s);
                }
            }
            if (words.size() != 0) for (int j = 0; j < words.size(); j++)
            {
                if (j == 2) ans.add(words.get(j));
                if (j == 0)
                {
                    for (int k = 0; k < Varity.size(); k++) ans.add(Varity.get(k));
                    ans.add(words.get(j));
                    for (int k = 0; k < Objact.size(); k++) ans.add(Objact.get(k));
                }
                if (j == 1) ans.add(words.get(j));
            }
            else
            {
                for (int k = 0; k < Varity.size(); k++) ans.add(Varity.get(k));
                for (int k = 0; k < Objact.size(); k++) ans.add(Objact.get(k));
            }
        } else if (i == 1)
        {

            int size = pwords.size();
            for (int j = 0, k = 0; j < size; j++)
            {
                String s = pwords.get(j);
                for (k = 0; k < type.size(); k++) if (s.equals(type.get(k))) break;
                if (k == type.size()) words.add(s);
            }

            if (words.size() != 0) for (int j = 0; j < words.size(); j++)
            {
                if (j == 2) ans.add(words.get(j));
                if (j == 0)
                {
                    ans.add(words.get(j));
                    for (int k = 0; k < type.size(); k++) ans.add(type.get(k));
                }
                if (j == 1) ans.add(words.get(j));
            }
            else for (int k = 0; k < type.size(); k++) ans.add(type.get(k));

        } else if (i == 2)
        {
            int size = pwords.size();
            for (int j = 0, k = 0; j < size; j++)
            {
                String s = pwords.get(j) + "()";
                for (k = 0; k < Method.size(); k++) if (s.equals(Method.get(k))) break;
                if (k == Method.size()) words.add(s);
            }

            if (words.size() != 0) for (int j = 0; j < words.size(); j++)
            {
                if (j == 2) ans.add(words.get(j));
                if (j == 0)
                {
                    for (int k = 0; k < Method.size(); k++) ans.add(Method.get(k));
                    ans.add(words.get(j));
                }
                if (j == 1) ans.add(words.get(j));
            }
            else
            {
                for (int k = 0; k < Method.size(); k++) ans.add(Method.get(k));
            }
        } else if (i == 3)
        {
            for (int j = 0; j < pwords.size() && j < 7; j++)
            {
                ans.add(pwords.get(j));
            }
        }
    }


    public static ArrayList<String> getType (String text,String[] kwd, String[] kwd2)
    {
        ArrayList<String> ans = new ArrayList<>();
        String [] words = text.split("\\s+");
        for (int i =0 ; i<words.length;i++) {
            if(words[i].length()>0 && words[i].charAt(0)>='A' && words[i].charAt(0) <='Z' ) {
                String s  = readvalue(words[i]);
                if(s.equals("")==false && ans.contains(words[i]) == false) ans.add(words[i]);
            }
        }
        for (int i = 0; i <= 3 ; i++)
        {
            ans.add(kwd[i]);
        }
        return ans;//修改3
    }


    public static ArrayList<String> getMethod (String text, ArrayList<String> Objact)
    {

        ArrayList<String> ans = new ArrayList<>();
        String[] lines = text.split("\n");
        String methodpattern = "\\s*+\\.[a-zA-Z1-9_]+\\s*+";
        for (int i = 0; i < Objact.size(); i++)
            for (int j = 0; j < lines.length; j++)
            {
                Matcher m = getMatcher(lines[j], Objact.get(i) + methodpattern);
                if (m.find() && ans.contains(m.group(0)+"()") == false )
                {
                    int r = m.start(); if(r>0 ) { char c = lines[j].charAt(r-1);if (Character.isLetter(c)) continue; }
                    ans.add(m.group(0) + "()");
                    System.out.println("method :" + "   " + m.group(0));
                }
            }
        return ans;
    }

    public static ArrayList<String> getVarity (String text, String[] kwd, int model)
    {

        String[] lines = text.split("\n");
        ArrayList<String> keywords = new ArrayList<>();
        ArrayList<String> DIY = new ArrayList<>();
        ArrayList<String> base = new ArrayList<>();
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        HashMap<String, String> map2 = new HashMap<>();

        if (model == 1) GetDiy(lines, keywords);
        else { for (int i = 0; i < kwd.length; i++) keywords.add(kwd[i]);}//修改1
        getbasevarity(map, map2, base, keywords, lines, model);


        String[] words = text.split(" ");
        ArrayList<Varity> varity = new ArrayList<>();
        for (int i = 0; i < base.size(); i++)
        {
            int cnt = 0;
            for (int j = 0; j < words.length; j++)
            {
                String x = readvalue(words[j]);
                if (x.equals(base.get(i))) cnt++;
            }
            String x = base.get(i);
            varity.add(new Varity(x, cnt, map.get(x), map2.get(x)));
        }
        sort(varity);
        ArrayList<String> ans = new ArrayList<>();
        for (int i = 0; i < varity.size(); i++)
        {
            System.out.print(varity.get(i).body + "  ");
            System.out.print((varity.get(i)).type + " ");
            System.out.println(varity.get(i).times + " " + varity.get(i).distance);
            ans.add(varity.get(i).body);
        }
        return ans;
    }


    public static void judgewordsislegal (String[] words, ArrayList<String> base, HashMap<String, String> mp2, HashMap<String, Integer> mp, String keyword, int j)
    {
        int flag = 0;
        for (int k = 0; k < words.length; k++)
        {
            if (words[k].equals(keyword))
            {
                k++;
                int cnt = 0;
                while (k < words.length && cnt < 5)
                {
                    if (isVarity(words[k]))
                    {
                        String x = readvalue(words[k]);
                        if (x.equals("")) break;
                        if (!base.contains(x))
                        {
                            base.add(x);
                            mp2.put(x, keyword);
                        }
                        mp.put(x, j);
                        flag = 1;
                        break;
                    }
                    cnt++;
                    k++;
                }
                if (flag == 1)
                {
                    break;
                }
            }
        }
    }

    public static void judgeobjectislegal (HashMap<String, Integer> mp, HashMap<String, String> mp2, ArrayList<String> base, String keyword, String[] lines)
    {
        String pattern = "\\s*+<\\s*+([a-zA-Z0-9]+)?\\s*+>\\s*+";

        if (keyword.contains("<>"))
        {
            keyword = keyword.substring(0, keyword.length() - 2);
            for (int i = 0; i < lines.length; i++)
            {
                Matcher p = getMatcher(lines[i], keyword + pattern);
                if (p.find())
                {
                    int start = p.end();
                    String s = "";
                    for (int j = start; lines[i].charAt(j) != ' '; j++) s += lines[i].charAt(j);
                    if (isVarity(s))
                    {
                        if (!base.contains(s))
                        {
                            base.add(s);
                            mp2.put(s, keyword);
                        }
                        mp.put(s, i);
                    }
                    continue;
                }
            }
        } else
        {
            for (int j = 0; j < lines.length; j++)
            {
                String[] words = lines[j].split("\\s+");
                judgewordsislegal(words, base, mp2, mp, keyword, j);
            }
        }
    }


    public static void getbasevarity (HashMap<String, Integer> mp1, HashMap<String, String> mp2, ArrayList<String> base, ArrayList<String> keywords, String[] lines, int model)
    {
        if (model == 0) for (int i = 0; i < keywords.size(); i++)
        {
            for (int j = 0; j < lines.length; j++)
            {
                String[] words = lines[j].split("\\s+");
                judgewordsislegal(words, base, mp2, mp1, keywords.get(i), j);
            }
        }
        else if (model == 1) for (int i = 0; i < keywords.size(); i++)
            judgeobjectislegal(mp1, mp2, base, keywords.get(i), lines);

    }


    public static void GetDiy (String[] lines, ArrayList<String> keywords)
    {
        for (int i = 0; i < lines.length; i++)
        {
            String[] words = lines[i].split("\\s+");
            for (int k = 0; k < words.length; k++)
            {
                if (words[k].length()>0 && words[k].charAt(0)>='A' &&words[k].charAt(0) <='Z' )
                {
                    String x = readvalue(words[k]);
                    if (x.equals("") == false && keywords.contains(words[k]) ==false) keywords.add(words[k]);
                    break;
                }
            }
        }
    }
    //修改2

    public static String readvalue (String word)
    {
        String value = "";
        if (word.length() == 0) return "";
        char c = word.charAt(0);
        int i = 0;
        while (Character.isLetter(c) || Character.isDigit(c) || c == '_')
        {
            value += c;
            i++;
            if (i < word.length()) c = word.charAt(i);
            else break;
        }
        if (i < word.length())
        {
            String x = "\\([<>a-zA-Z0-9\\._]*+[\\)]*+";
            Matcher m = getMatcher(word, x);
            if (m.find(i)) return "";
            if(word.contains("(")) return "";
            if(word.contains(")")) return "";
        }
        if (value.equals("implements") || value.equals("extends") || value.equals("interface") || value.equals("abstract"))
            return "";
        return value;
    }

    private static Matcher getMatcher (String line, String pattern)
    {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(line);
        return m;
    }

    public static boolean isVarity (String word)
    {
        char c = word.charAt(0);
        if (word.charAt(0) != '_' && Character.isLetter(c) == false)
        {
            return false;
        }
        return true;
    }
}

class  Varity implements Comparable
{
    public int times;
    public int distance;
    public String body;
    public String type;

    Varity (String body, int times, int distance, String type)
    {
        this.body = body;
        this.times = times;
        this.distance = distance;
        this.type = type;
    }

    @Override
    public int compareTo (Object o)
    {
        Varity a = (Varity) o;
        long suma = a.distance * a.times;
        long sum = distance * times;
        if (sum < suma) return 1;
        else if (sum == suma) return 0;
        else return -1;
    }
/*
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
       */
}

