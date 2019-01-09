package com.hualala.boss.weixin.controller;

import com.hualala.boss.weixin.Constants.WorkWeixinConstant;
import com.hualala.boss.weixin.model.CallBackModel;
import com.hualala.boss.weixin.mp.aes.AesException;
import com.hualala.boss.weixin.mp.aes.WXBizMsgCrypt;
import com.hualala.boss.weixin.service.inter.RedisService;
import com.hualala.boss.weixin.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

/**
 * @author liwenxing
 * @date 2019/1/3 15:32
 */
@RestController
@RequestMapping("/weixin")
public class WeixinController {

    @Autowired
    RedisService redisService;
    /**
     * 数据回调地址 地址验证
     *
     * @param callBackModel
     * @return
     * @throws AesException
     */
    @RequestMapping(value = "/datarecive", method = RequestMethod.GET)
    public String dataVerify(CallBackModel callBackModel) throws AesException {
        WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(WorkWeixinConstant.STOKEN, WorkWeixinConstant.SENCODINGAESKEY, WorkWeixinConstant.SCORPID);
        callBackModel.setEchostr(callBackModel.getEchostr().replace(" ", "+"));
        String result = wxcpt.VerifyURL(callBackModel.getMsg_signature(), callBackModel.getTimestamp(),
                callBackModel.getNonce(), callBackModel.getEchostr());
//        System.out.println(callBackModel.getEchostr());
//        System.out.println("verifyurl echostr: " + result);
        return result;
    }

    /**
     * 数据回调地址 接收数据
     * @param callBackModel
     * @param request
     * @param response
     * @return
     * @throws AesException
     */
    @RequestMapping(value = "/datarecive", method = RequestMethod.POST)
    public String getDataRecive(CallBackModel callBackModel, HttpServletRequest request, HttpServletResponse response) throws AesException {
        System.out.println(request.toString());
        return "success";
    }

    /**
     * 指令回调地址 地址验证
     * @param callBackModel
     * @return
     * @throws AesException
     */
    @RequestMapping(value = "/directrecive", method = RequestMethod.GET)
    public String directVerify(CallBackModel callBackModel) throws AesException {
        WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(WorkWeixinConstant.STOKEN, WorkWeixinConstant.SENCODINGAESKEY, WorkWeixinConstant.SCORPID);
        callBackModel.setEchostr(callBackModel.getEchostr().replace(" ", "+"));
        String result = wxcpt.VerifyURL(callBackModel.getMsg_signature(), callBackModel.getTimestamp(),
                callBackModel.getNonce(), callBackModel.getEchostr());
//        System.out.println(callBackModel.getEchostr());
//        System.out.println("verifyurl echostr: " + result);
        return result;
    }

    /**
     * 指令回调地址 接收数据
     *
     * @param callBackModel
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/directrecive", method = RequestMethod.POST)
    public String getDirectRecive(CallBackModel callBackModel, HttpServletRequest request, HttpServletResponse response) throws Exception {
        StringBuffer data = new StringBuffer();
        String line = null;
        BufferedReader reader = null;
        try {
            reader = request.getReader();
            while (null != (line = reader.readLine()))
                data.append(line);
        } catch (IOException e) {
        } finally {
        }
        System.out.println(data.toString());
        WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(WorkWeixinConstant.STOKEN, WorkWeixinConstant.SENCODINGAESKEY, WorkWeixinConstant.SUITEID);
        String sMsg = wxcpt.DecryptMsg(callBackModel.getMsg_signature(), callBackModel.getTimestamp(), callBackModel.getNonce(), data.toString());
        System.out.println("after decrypt msg: " + sMsg);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        StringReader sr = new StringReader(sMsg);
        InputSource is = new InputSource(sr);
        Document document = db.parse(is);

        Element root = document.getDocumentElement();
        NodeList nodelist1 = root.getElementsByTagName("SuiteTicket");
        if (nodelist1 != null && nodelist1.item(0) != null) {
            String Content = nodelist1.item(0).getTextContent();
            System.out.println("SuiteTicket：" + Content);
            redisService.setValueForTime(RedisUtils.BOSS_WORKWEIXIN_SUITETICKET, Content, 60 * 30);
        }

        return "success";
    }

}
