package org.dync.giftlibrary.util;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Allen on 2017/1/8.
 */
public class GiftAnimationUtil {


    /**
     * @param target
     * @param star     动画起始坐标
     * @param end      动画终止坐标
     * @param duration 持续时间
     * @return 创建一个从左到右的飞入动画
     * 礼物飞入动画
     */
    public static ObjectAnimator createFlyFromLtoR(final View target, float star, float end, int duration, TimeInterpolator interpolator) {
        //1.个人信息先飞出来
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(target, "translationX",
                star, end);
        anim1.setInterpolator(interpolator);
        anim1.setDuration(duration);
        return anim1;
    }


    /**
     * @param target
     * @return 播放帧动画
     */
    public static AnimationDrawable startAnimationDrawable(ImageView target) {
        AnimationDrawable animationDrawable = (AnimationDrawable) target.getDrawable();
        if (animationDrawable != null) {
            target.setVisibility(View.VISIBLE);
            animationDrawable.start();
        }
        return animationDrawable;
    }


    /**
     * @param target
     * @param drawable 设置帧动画
     */
    public static void setAnimationDrawable(ImageView target, AnimationDrawable drawable) {

        target.setBackground(drawable);
    }


    /**
     * @param target
     * @return 送礼数字变化
     */
    public static ObjectAnimator scaleGiftNum(final TextView target) {
        PropertyValuesHolder anim4 = PropertyValuesHolder.ofFloat("scaleX",
                1.2f, 0.9f, 1f);
        PropertyValuesHolder anim5 = PropertyValuesHolder.ofFloat("scaleY",
                1.2f, 0.9f, 1f);
        PropertyValuesHolder anim6 = PropertyValuesHolder.ofFloat("alpha",
                1.0f, 0.2f, 1f);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(target, anim4, anim5, anim6).setDuration(200);
        return animator;

    }


    /**
     * @param target
     * @param star
     * @param end
     * @param duration
     * @param startDelay
     * @return 向上飞 淡出
     */
    public static ObjectAnimator createFadeAnimator(final View target, float star, float end, int duration, int startDelay) {

        //TODO CQ 时间紧急 直接去掉了 上移动画  后面 需要 新增 不要胡乱修改
        PropertyValuesHolder translationY = PropertyValuesHolder.ofFloat("translationY", star, end);
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 1.0f, 0f);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(target, translationY, alpha);
        animator.setStartDelay(startDelay);
        animator.setDuration(duration);
        return animator;
    }


    /**
     * @param target
     * @param duration
     * @param startDelay
     * @return 修改了 原本的淡出动画逻辑
     */
    public static ObjectAnimator createFadeAnimator(final View target,int duration, int startDelay) {

        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 1.0f, 0f);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(target, alpha);
        animator.setStartDelay(startDelay);
        animator.setDuration(duration);
        return animator;
    }


    /**
     * @param animator1
     * @param animator2
     * @return 按顺序播放动画
     */
    public static AnimatorSet startAnimation(ObjectAnimator animator1, ObjectAnimator animator2) {
        AnimatorSet animSet = new AnimatorSet();
//        animSet.playSequentially(animators);
        animSet.play(animator1).before(animator2);
        animSet.start();
        return animSet;
    }

    /**
     * @param animator1
     * @return 按顺序播放动画
     */
    public static AnimatorSet startAnimation(ObjectAnimator animator1) {
        AnimatorSet animSet = new AnimatorSet();
//        animSet.playSequentially(animators);
        animSet.play(animator1);
        animSet.start();
        return animSet;
    }

    /**
     * @param animator1
     * @param animator2
     * @param animator3
     * @param animator4
     * @param animator5
     * @return 按顺序播放动画
     */
    public static AnimatorSet startAnimation(ObjectAnimator animator1, ObjectAnimator animator2, ObjectAnimator animator3, ObjectAnimator animator4, ObjectAnimator animator5) {
        AnimatorSet animSet = new AnimatorSet();
//        animSet.playSequentially(animators);
        animSet.play(animator1).before(animator2);
        animSet.play(animator3).after(animator2);
        animSet.play(animator4).after(animator3);
        animSet.play(animator5).after(animator4);
        animSet.start();
        return animSet;
    }




    /*
    *
    *
    *  新增修改
    *
    *
    * */

    /**
     * @param target
     * @param duration
     * @param startDelay
     * @return
     */
    public static ObjectAnimator createAlphaAnimator(final View target,int duration, int startDelay) {

        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 0.2f, 1.0f);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(target, alpha);
        animator.setStartDelay(startDelay);
        animator.setDuration(duration);
        return animator;
    }


    /**
     * @param animator1
     * @param animator2
     * @return 一起播放动画
     */
    public static AnimatorSet startTogetherAnimation(ObjectAnimator animator1, ObjectAnimator animator2, ObjectAnimator animator3, ObjectAnimator animator4) {
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(animator1).with(animator2);
        animSet.play(animator3).after(animator1);
        animSet.play(animator4).after(animator3);
        animSet.start();
        return animSet;
    }


    /**
     * translationX 动画
     * @param target
     * @param star     动画起始坐标
     * @param end      动画终止坐标
     * @param duration 持续时间
     * @param startDelay 延迟时间
     * @return 创建一个从左到右的飞入动画
     */
    public static ObjectAnimator createFlyFromLtoR(final View target, float star, float end, int duration, TimeInterpolator interpolator, int startDelay) {
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(target, "translationX",
                star, end);
        anim1.setInterpolator(interpolator);
        anim1.setStartDelay(startDelay);
        anim1.setDuration(duration);
        return anim1;
    }

    /**
     * translationX 动画
     * @param target
     * @param star     动画起始坐标
     * @param end      动画终止坐标
     * @param duration 持续时间
     * @param startDelay 延迟时间
     * @return 创建一个从右到左的飞入动画
     *
     *  end , star 取反了 注意
     */
    public static ObjectAnimator createFlyFromRtoL(final View target, float star, float end, int duration, TimeInterpolator interpolator, int startDelay) {
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(target, "translationX",
                end , star);
        anim1.setInterpolator(interpolator);
        anim1.setStartDelay(startDelay);
        anim1.setDuration(duration);
        return anim1;
    }


    /**
     * translationY 动画
     * @param target
     * @param star     动画起始坐标
     * @param end      动画终止坐标
     * @param duration 持续时间
     * @param startDelay 延迟时间
     * @return 创建一个从左到右的飞入动画
     */
    public static ObjectAnimator createFlyFromTtoB(final View target, float star, float end, int duration, TimeInterpolator interpolator, int startDelay) {
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(target, "translationY",
                star, end);
        anim1.setInterpolator(interpolator);
        anim1.setStartDelay(startDelay);
        anim1.setDuration(duration);
        return anim1;
    }

    /**
     * translationY 动画
     * @param target
     * @param star     动画起始坐标
     * @param end      动画终止坐标
     * @param duration 持续时间
     * @param startDelay 延迟时间
     * @return 创建一个从左到右的飞入动画
     */
    public static ObjectAnimator createFlyFromBtoT(final View target, float star, float end, int duration, TimeInterpolator interpolator, int startDelay) {
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(target, "translationY",
                end, star);
        anim1.setInterpolator(interpolator);
        anim1.setStartDelay(startDelay);
        anim1.setDuration(duration);
        return anim1;
    }
}
