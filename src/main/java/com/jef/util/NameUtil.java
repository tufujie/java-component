package com.jef.util;

import java.util.Random;

/**
 * @author Jef
 * @date 2022/9/30
 */
public class NameUtil {


    String familyName1 = "赵钱孙李周吴郑王冯陈褚卫蒋沈韩杨朱秦尤许何吕施张孔曹严华金魏陶姜戚谢邹喻水云苏潘葛奚范彭郎鲁韦昌马苗凤花方俞任袁柳鲍史唐费岑薛雷贺倪汤滕殷罗毕郝邬安常乐于时傅卞齐康伍余元卜顾孟平"
            + "黄和穆萧尹姚邵湛汪祁毛禹狄米贝明臧计成戴宋茅庞熊纪舒屈项祝董粱杜阮席季麻强贾路娄危江童颜郭梅盛林刁钟徐邱骆高夏蔡田胡凌霍万柯卢莫房缪干解应宗丁宣邓郁单杭洪包诸左石崔吉"
            + "龚程邢滑裴陆荣翁荀羊甄家封芮储靳邴松井富乌焦巴弓牧隗山谷车侯伊宁仇祖武符刘景詹束龙叶幸司韶黎乔苍双闻莘劳逄姬冉宰桂牛寿通边燕冀尚农温庄晏瞿茹习鱼容向古戈终居衡步都耿满弘国文东殴沃曾关红游盖益桓公晋楚闫";
    String familyName2 = "欧阳太史端木上官司马东方独孤南宫万俟闻人夏侯诸葛尉迟公羊赫连澹台皇甫宗政濮阳公冶太叔申屠公孙慕容仲孙钟离长孙宇文司徒鲜于司空闾丘子车亓官司寇巫马公西颛孙壤驷公良漆雕乐正宰父谷梁拓跋夹谷轩辕令狐段干百里呼延东郭南门羊舌微生公户公玉公仪梁丘公仲公上公门公山公坚左丘公伯西门公祖第五公乘贯丘公皙南荣东里东宫仲长子书子桑即墨达奚褚师吴铭";
    String girlName = "秀娟英华慧巧美娜静淑惠珠翠雅芝玉萍红娥玲芬芳燕彩春菊兰凤洁梅琳素云莲真环雪荣爱妹霞香月莺媛艳瑞凡佳嘉琼勤珍贞莉桂娣叶璧璐娅琦晶妍茜秋珊莎锦黛青倩婷姣婉娴瑾颖露瑶怡婵雁蓓纨仪荷丹蓉眉君琴蕊薇菁梦岚苑婕馨瑗琰韵融园艺咏卿聪澜纯毓悦昭冰爽琬茗羽希宁欣飘育滢馥筠柔竹霭凝晓欢霄枫芸菲寒伊亚宜可姬舒影荔枝思丽";
    String boyName = "伟刚勇毅俊峰强军平保东文辉力明永健世广志义兴良海山仁波宁贵福生龙元全国胜学祥才发武新利清飞彬富顺信子杰涛昌成康星光天达安岩中茂进林有坚和彪博诚先敬震振壮会思群豪心邦承乐绍功松善厚庆磊民友裕河哲江超浩亮政谦亨奇固之轮翰朗伯宏言若鸣朋斌梁栋维启克伦翔旭鹏泽晨辰士以建家致树炎德行时泰盛雄琛钧冠策腾楠榕风航弘";
    String boyNameV2 = "伟刚勇毅俊峰强军平保东文辉力明永健世广志义兴良海山仁波宁贵福生龙元全国胜学祥才发武新利清飞彬富顺信子杰涛昌成康星光天达安岩中茂进林有坚和彪博诚先敬震振壮会思群豪心邦承乐绍功松善厚庆磊民友裕河哲江超浩亮政谦亨奇固之轮翰朗伯宏言若鸣朋斌梁栋维启克伦翔旭鹏泽晨辰士以建家致树炎德行时泰盛雄琛钧冠策腾楠榕风航弘桦耀骏崇广驰泉谚凌生诤康海殿之山功河巩襦全升正铠鹰晷胜科诚朕锦舱达璟俯瑞冠容固承儒彤佚翰立俊豪瀚融增纲毅澔意铖迅章彰谦乔振华禅恬澎部瑾钦展采景励彦鹏隆福艺力喆为宜频菁基亿佳茵苓韦澉涛驹震凤万大颔辅睿谱议尧畅襦敬善进梁以哲候真超晋浦来利钢英珑阔娟刚高聚理斌颜弛庆军垒采亦若朗锐森奕艺照淇帝砂兴基歌焘卿亨珺礼吉雄光圣政盈代鑫镇竣迎贵弓骅亮逸澉钧聪筠谊丞颔飒涓波溢榕富炎易韩昭益沧星茹绮劭博臻罡钊义琛滢社征凯寒晟";

    /**
     * 功能：随机产生姓氏
     *
     * @return
     */
    public String getFamilyName() {
        String str = "";
        int randNum = new Random().nextInt(2) + 1;
        int strLen = randNum == 1 ? familyName1.length() : familyName2.length();
        int index = new Random().nextInt(strLen);
        if (randNum == 1) {
            str = String.valueOf(familyName1.charAt(index));
        } else {
            str = (index & 1) == 0 ? familyName2.substring(index, index + 2) :
                    familyName2.substring(index - 1, index + 1);
        }
        return str;
    }

    /**
     * 功能：随机产生性别
     *
     * @return
     */
    public String getSex() {
        int randNum = new Random().nextInt(2) + 1;
        return randNum == 1 ? "男" : "女";
    }

    /**
     * 功能：传入性别参数，依据性别产生名字
     *
     * @param sex
     * @param randNum 名字字数
     * @return
     */
    public String getName(String sex, String lastName, int randNum) {
        int strLen = sex.equals("男") ? boyName.length() : girlName.length();
        int index = (randNum & 1) == 0 ? new Random().nextInt(strLen - 1) :
                new Random().nextInt(strLen);
        String name = lastName;
        name += sex.equals("男") ? boyName.substring(index, index + randNum) :
                girlName.substring(index, index + randNum);
        return name;
    }

    /**
     * 功能：传入性别参数，依据性别产生名字
     *
     * @param sex
     * @param randNum 名字字数
     * @return
     */
    public String getNameV2(String sex, String lastName, String secondName, String thirdName) {
        int strLen = sex.equals("男") ? boyNameV2.length() : girlName.length();
        String name = lastName;
        int index = 0;
        if (secondName == null || secondName == "") {
            index = new Random().nextInt(strLen - 1);
            name += sex.equals("男") ? boyNameV2.substring(index, index + 1) :
                    girlName.substring(index, index + 1);
        } else {
            name += secondName;
        }
        if (thirdName == null || thirdName == "") {
            index = new Random().nextInt(strLen - 1);
            name += sex.equals("男") ? boyNameV2.substring(index, index + 1) :
                    girlName.substring(index, index + 1);
        } else {
            name += thirdName;
        }
        return name;
    }


    /**
     * 功能：随机产生18-21的整数
     *
     * @return
     */
    public int getAge() {
        return new Random().nextInt(4) + 18;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            System.out.println(new NameUtil().getNameV2("男", "涂", "", "泽"));
        }
    }
}