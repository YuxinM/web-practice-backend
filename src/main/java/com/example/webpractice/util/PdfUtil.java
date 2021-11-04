package com.example.webpractice.util;


import com.example.webpractice.vo.AnalyseVO;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.text.pdf.draw.LineSeparator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
/**
 * PDF生成工具类
 * @Author MengYuxin
 * @Date 2021/11/4 14:47
 */
@Slf4j
public class PdfUtil {

    // 定义全局的字体静态变量
    private static Font titlefont;
    private static Font headfont;
    private static Font keyfont;
    private static Font textfont;

    // 静态代码块
    static {
        try {
            // 不同字体（这里定义为同一种字体：包含不同字号、不同style）
            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            titlefont = new Font(bfChinese, 16, Font.BOLD);
            headfont = new Font(bfChinese, 14, Font.BOLD);
            keyfont = new Font(bfChinese, 10, Font.BOLD);
            textfont = new Font(bfChinese, 10, Font.NORMAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void make(String path, AnalyseVO analyseVO) {
        try {
            // 1.新建document对象
            Document document = new Document(PageSize.A4);// 建立一个Document对象

            // 建立一个书写器(Writer)与document对象关联
            File file = new File(path);
            file.createNewFile();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));

            // 3.打开文档
            document.open();
            document.addTitle("Title@PDF-Java");// 标题
            document.addAuthor("Author@umiz");// 作者
            document.addSubject("Subject@iText pdf sample");// 主题
            document.addKeywords("Keywords@iTextpdf");// 关键字
            document.addCreator("Creator@umiz`s");// 创建者

            // 4.向文档中添加内容
            generatePDF(document,analyseVO);

            // 5.关闭文档
            document.close();
        } catch (Exception e) {
            log.warn("创建pdf出错");
            e.printStackTrace();
        }
    }

    // 生成PDF文件
    public static void generatePDF(Document document,AnalyseVO analyseVO) throws Exception {

        // 段落
        Paragraph paragraph = new Paragraph(analyseVO.getNumber(), titlefont);
        paragraph.setAlignment(1); //设置文字居中 0靠左   1，居中     2，靠右
//        paragraph.setIndentationLeft(12); //设置左缩进
//        paragraph.setIndentationRight(12); //设置右缩进
//        paragraph.setFirstLineIndent(24); //设置首行缩进
        paragraph.setLeading(20f); //行间距
        paragraph.setSpacingBefore(5f); //设置段落上空白
        paragraph.setSpacingAfter(10f); //设置段落下空白

        // 直线
        Paragraph p1 = new Paragraph();
        p1.add(new Chunk(new LineSeparator()));

        Paragraph p2=new Paragraph("法规文号",headfont);
        p1.setAlignment(0);
        Paragraph number=new Paragraph(analyseVO.getNumber(),textfont);

        Paragraph p3=new Paragraph("效力等级",headfont);
        p2.setAlignment(0);
        Paragraph category=new Paragraph(analyseVO.getCategory(),textfont);

        Paragraph p4=new Paragraph("解释部门",headfont);
        p3.setAlignment(0);
        Paragraph interpret = new Paragraph(analyseVO.getInterpret_department(), textfont);

        Paragraph p5 = new Paragraph("录入时间", headfont);
        p5.setAlignment(0);
        Paragraph input_time = new Paragraph(analyseVO.getInput_time(), textfont);

        Paragraph p6 = new Paragraph("正文", headfont);
        p6.setAlignment(0);
        Paragraph content = new Paragraph(analyseVO.getContent().replace("\n",""), textfont);

        document.add(paragraph);
        document.add(p1);
        document.add(p2);
        document.add(number);
        document.add(p3);
        document.add(category);
        document.add(p4);
        document.add(interpret);
        document.add(p5);
        document.add(input_time);
        document.add(p6);
        document.add(content);
    }

//    public static void main(String[] args) throws Exception {
//        AnalyseVO analyseVO=new AnalyseVO();
//        analyseVO.setTitle("中国人民银行令〔2019〕第4号（应收账款质押登记办法）");
//        analyseVO.setCategory("部门");
//        analyseVO.setNumber("银行令〔2019〕第4号");
//        analyseVO.setInput_time("2021年11月4日");
//        analyseVO.setInterpret_department("银行");
//        analyseVO.setContent("\"中国人民银行令\n" +
//                "(2019)第4号\n" +
//                "《应收账款质押登记办法》已经2019年9月18日中国人民\n" +
//                "银行2019年第1次行务会议审议通过,现予发布,自2020年1\n" +
//                "月1日起施行\n" +
//                "易\n" +
//                "2019年11月22日\n" +
//                "\f\n" +
//                "应收账款质押登记办法\n" +
//                "第一章总则\n" +
//                "第一条为规范应收账款质押登记,保护质押当事人和利害\n" +
//                "关系人的合法权益,根据《中华人民共和国物权法》等相关法律\n" +
//                "规定,制定本办法\n" +
//                "第二条本办法所称应收账款是指权利人因提供一定的货\n" +
//                "物、服务或设施而获得的要求义务人付款的权利以及依法亨有的\n" +
//                "其他付款请求权,包括现有的和未来的金钱债权,但不包括因票\n" +
//                "据或其他有价证券而产生的付款请求权,以及法律、行政法规禁\n" +
//                "止转让的付款请求权\n" +
//                "本办法所称的应收账款包括下列权利\n" +
//                "(一)销售、出租产生的债权,包括销售货物,供应水、电、\n" +
//                "气、暖,知识产权的许可使用,出租动产或不动产等\n" +
//                "(二)提供医疗、教育、旅游等服务或劳务产生的债权;\n" +
//                "(三)能源、交通运输、水利、环境保护、市政工程等基础\n" +
//                "设施和公用事业项目收益权;\n" +
//                "(四)提供贷款或其他信用活动产生的债权;\n" +
//                "(五)其他以合同为基础的具有金钱给付内容的债权\n" +
//                "\f\n" +
//                "第三条本办法所称应收账款质押是指《中华人民共和国物\n" +
//                "权法》第二百二十三条规定的应收账款出质,具体是指为担保债\n" +
//                "务的履行,债务人或者第三人将其合法拥有的应收账款出质给债\n" +
//                "权人,债务人不履行到期债务或者发生当事人约定的实现质权的\n" +
//                "情形,债权人有权就该应收账款及其收益优先受偿\n" +
//                "前款规定的债务人或者第三人为出质人,债权人为质杈人\n" +
//                "第四条中国人民银行征信中心(以下简称征信中心)是应收\n" +
//                "账款质押的登记机构\n" +
//                "征信中心建立基于互联网的登记公示系统(以下简称登记公\n" +
//                "示系统),办理应收账款质押登记,并为社会公众提供查询服务\n" +
//                "第五条中国人民银行对征信中心办理应收账款质押登记有\n" +
//                "关活动进行管理\n" +
//                "第六条在同一应收账款上设立多个权利的,质杈人按照登\n" +
//                "记的先后顺序行使质权\n" +
//                "第二章登记与查询\n" +
//                "第七条应收账款质押登记通过登记公示系统办理\n" +
//                "第八条应收账款质押登记由质权人办理。质权人办理质押\n" +
//                "登记的,应当与出质人就登记内容达成一致\n" +
//                "质权人也可以委托他人办理登记。委托他人办理登记的,适\n" +
//                "用本办法关于质权人办理登记的规定\n" +
//                "\f\n" +
//                "第九条质权人办理应收账款质押登记时,应当注册为登记\n" +
//                "公示系统的用户\n" +
//                "第十条登记内容包括质权人和出质人的基本信息、应收账\n" +
//                "款的描述、登记期限\n" +
//                "出质人或质权人为单位的,应当填写单位的法定注册名称\n" +
//                "住所、法定代表人或负责人姓名、组织机构代码或金融机构编码\n" +
//                "工商注册号、法人和其他组织统一社会信用代码、全球法人机构\n" +
//                "别编码等机构代码或编码\n" +
//                "出质人或质权人为个人的,应当填写有效身份证件号码、有\n" +
//                "效身份证件载明的地址等信息\n" +
//                "质权人可以与出质人约定将主债权金额等项目作为登记内\n" +
//                "容\n" +
//                "第十一条质权人应当将填写完毕的登记内容提交登记公示\n" +
//                "系统。登记公示系统记录提交时间并分配登记编号,生成应收账\n" +
//                "款质押登记初始登记证明和修改码提供给质权人\n" +
//                "第十二条质权人应当根据主债杈履行期限合理确定登记期\n" +
//                "限。登记期限最短1个月,最长不超过30年\n" +
//                "第十三条在登记期限届满前90日内,质权人可以申请展期\n" +
//                "质权人可以多次展期,展期期限最短1个月,每次不得超过\n" +
//                "30年\n" +
//                "第十四条登记内容存在遗漏、错误等情形或登记内容发生\n" +
//                "变化的,质权人应当办理变更登记\n" +
//                "4\n" +
//                "\f\n" +
//                "质权人在原质押登记中增加新的应收账款出质的,新增加的\n" +
//                "部分视为新的质押登记\n" +
//                "第十五条质权人办理登记时所填写的岀质人法定注册名称\n" +
//                "或有效身份证件号码变更的,质权人应当在变更之日起4个月内\n" +
//                "办理变更登记\n" +
//                "第十六条质权人办理展期、变更登记的,应当与出质人就展\n" +
//                "期、变更事项达成一致\n" +
//                "第十七条有下列情形之一的,质权人应当自该情形产生之\n" +
//                "日起10个工作日内办理注销登记\n" +
//                "(一)主债权消灭;\n" +
//                "(二)质权实现;\n" +
//                "(三)质权人放弃登记载明的应收账款之上的全部质权;\n" +
//                "(四)其他导致所登记权利消灭的情形\n" +
//                "质权人迟延办理注销登记,给他人造成损害的,应当承担相\n" +
//                "应的法律责任\n" +
//                "第十八条质杈人凭修改码办理展期、变更登记、注销登记。\n" +
//                "第十九条出质人或其他利害关系人认为登记内容错误的,\n" +
//                "可以要求质权人变更登记或注销登记。质杈人不同意变更或注销\n" +
//                "的,出质人或其他利害关系人可以办理异议登记\n" +
//                "办理异议登记的出质人或其他利害关系人可以自行注销异\n" +
//                "议登记\n" +
//                "第二十条出质人或其他利害关系人应当在异议登记办理完\n" +
//                "\f\n" +
//                "毕之日起7日内通知质权人\n" +
//                "第二十一条出质人或其他利害关系人自异议登记之日起30\n" +
//                "日内,未将争议起诉或提请仲裁并在登记公示系统提交案件受理\n" +
//                "通知的,征信中心撤销异议登记\n" +
//                "第二十二条应出质人或其他利害关系人、质权人的申请,征\n" +
//                "信中心根据对出质人或其他利害关系人、质权人生效的法院判决\n" +
//                "裁定或仲裁机构裁决撤销应收账款质押登记或异议登记\n" +
//                "第二十三条质权人办理变更登记和注销登记、出质人或其\n" +
//                "他利害关系人办理异议登记后,登记公示系统记录登记时间、分\n" +
//                "配登记编号,并生成变更登记、注销登记或异议登记证明\n" +
//                "第二十四条质权人开展应收账款质押融资业务时,应当严\n" +
//                "格审核确认应收账款的真实性,并在登记公示系统中查询应收账\n" +
//                "款的权利负担状况\n" +
//                "第二十五条质权人、出质人和其他利害关系人应当按照登\n" +
//                "记公示系统提示项目如实登记,并对登记内容的真实性、完整性\n" +
//                "和合法性负责。办理登记时,存在提供虚假材料等行为给他人造\n" +
//                "成损害的,应当承担相应的法律责任\n" +
//                "第二十六条任何单位和个人均可以在注册为登记公示系统\n" +
//                "的用户后,查询应收账款质押登记信息\n" +
//                "第二十七条出质人为单位的,查询人以出质人的法定注册\n" +
//                "名称进行查询\n" +
//                "出质人为个人的,查询人以出质人的身份证件号码进行查\n" +
//                "\f\n" +
//                "询\n" +
//                "第二十八条征信中心根据查询人的申请,提供查询证明\n" +
//                "第二十九条质权人、出质人或其他利害关系人、查询人可以\n" +
//                "通过证明编号在登记公示系统对登记证明和查询证明进行验证\n" +
//                "第三章征信中心的职责\n" +
//                "第三十条征信中心应当采取技术措施和其他必要措施,维\n" +
//                "护登记公示系统安全、正常运行,防止登记信息泄露、丢失\n" +
//                "第三十一条征信中心应当制定登记操作规则和内部管理制\n" +
//                "度,并报中国人民银行备案\n" +
//                "第三十二条登记注销或登记期限届满后,征信中心应当对\n" +
//                "登记记录进行电子化离线保存,保存期限为15年\n" +
//                "第四章附则\n" +
//                "第三十三条征信中心按照国务院价格主管部门批准的收费\n" +
//                "标准收取应收账款登记服务费用\n" +
//                "第三十四条权利人在登记公示系统办理以融资为目的的应\n" +
//                "收账款转让登记,参照本办法的规定\n" +
//                "第三十五条权利人在登记公示系统办理其他动产和权利担\n" +
//                "保登记的,参照本办法的规定执行\n" +
//                "\f\n" +
//                "本办法所称动产和权利担保包括当事人通过约定在动产和\n" +
//                "权利上设定的、为偿付债务或以其他方式履行债务提供的、具有\n" +
//                "担保性质的各类交易形式,包括但不限于融资租赁、保证金质押\n" +
//                "存货和仓单质押等,法律法规另有规定的除外\n" +
//                "第三十六条本办法由中国人民银行负责解释\n" +
//                "第三十七条本办法自2020年1月1日起施行。《应收账款\n" +
//                "质押登记办法》(中国人民银行令〔2017第3号发布)同时废止\n" +
//                "信息公开选项:主动公开\n" +
//                "发\n" +
//                "新闻单位\n" +
//                "内部发送:办公厅、条法司、征信局、征信中心\n" +
//                "中国人民银行办公厅\n" +
//                "2019年11月26日印发\n" +
//                "\f\n" +
//                "\"\n");
//        make("D:/PDFDemo.pdf",analyseVO);
//    }


}
