package com.jef.util;

import org.springframework.web.servlet.ModelAndView;

/**
 * 统一页面跳转
 *
 * @author Jef
 * @create 2018/6/12 21:03
 */
public class BasicJspUtil {
    /**
     * 懒，编写一个demo时不用新页面时的跳转
     *
     * @return
     */
    public static ModelAndView getBasicView() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("basicJsp");
        return mv;
    }
}
