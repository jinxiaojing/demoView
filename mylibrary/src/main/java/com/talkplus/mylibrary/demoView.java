package com.talkplus.mylibrary;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.List;

public class demoView extends View {

    private float outsideW = 0;                     //外圆的宽度
    private float progressW = 0;                    //进度条的宽度
    private int progressBG;                         //进度条背景
    private int outsideBG;                          //外圆背景
    private int outsideDefultTC;                    //外圆默认字体颜色
    private int insideBG;                           //内圆背景
    private int angle = 0;                          //弧度
    private int ViewW = 0;                          //布局宽度
    private int ViewH = 0;                          //布局高度
    private List<String> outsideList;               //外圆文本集合
    private List<String> insideList;                //外圆文本集合
    private Path path;                              //进度条背景路径用于计算总长度
    private Path selPath;                           //进度条路径

    private Paint lintP;                            //进度条背景画笔
    private Paint lintPbg;                          //进度条背景画笔
    private Paint centerP;                          //中间文字画笔
    private Paint centerP1;                         //中间文字画笔
    private Paint outsideP;                         //外圆画笔
    private Paint outsideTextP;                     //外圆字体画笔
    private Paint insideTextP;                      //内圆字体画笔
    private Paint insideP;                          //外圆画笔
    private Paint bTextBP;                          //底部圆角矩形画笔
    private int select;
    private float itemW;
    private PathMeasure measure;

    public demoView(Context context) {
        this(context, null);
    }

    public demoView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public demoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    /**
     * 获取自定义值
     *
     * @param context
     * @param attrs
     */
    private void initView(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.demoViewStyle);
        progressW = typedArray.getDimension(R.styleable.demoViewStyle_progressW, 100);
        outsideW = typedArray.getDimension(R.styleable.demoViewStyle_outsideW, 40);
        angle = typedArray.getInteger(R.styleable.demoViewStyle_angle, 20);
        outsideBG = typedArray.getColor(R.styleable.demoViewStyle_outsideBG, Color.BLACK);
        progressBG = typedArray.getColor(R.styleable.demoViewStyle_progressBG, Color.BLACK);
        outsideDefultTC = typedArray.getColor(R.styleable.demoViewStyle_outsideDefultTextColor, Color.BLACK);
        insideBG = typedArray.getColor(R.styleable.demoViewStyle_insideBG, Color.BLACK);
        typedArray.recycle();
        initPaint();
    }

    private void initBGPath() {
        path = new Path();
        RectF ovalLeft = new RectF(outsideW + progressW / 2, outsideW + progressW / 2, ViewH - outsideW - progressW, ViewH - outsideW - progressW / 2);
        RectF ovalRight = new RectF(ViewW - ViewH + outsideW + progressW / 2, outsideW + progressW / 2, ViewW - outsideW - progressW / 2, ViewH - outsideW - progressW / 2);
        path.moveTo(ViewW / 2, ViewH - outsideW - progressW / 2);
        path.lineTo(ViewH / 2, ViewH - outsideW - progressW / 2);
        path.arcTo(ovalLeft, 90, 180);
        path.lineTo(ViewW - ViewH / 2, outsideW + progressW / 2);
        path.arcTo(ovalRight, 270, 180);
        path.lineTo(ViewW / 2, ViewH - outsideW - progressW / 2);
    }

    private void initPaint() {
        outsideP = new Paint();
        outsideTextP = new Paint();
        insideTextP = new Paint();
        insideP = new Paint();
        lintP = new Paint();
        lintPbg = new Paint();
        centerP = new Paint();
        centerP1 = new Paint();
        bTextBP = new Paint();

        outsideP.setStyle(Paint.Style.STROKE);
        outsideP.setStrokeWidth(outsideW);
        outsideP.setAntiAlias(true);
        outsideP.setColor(outsideBG);

        insideP.setAntiAlias(true);
        insideP.setColor(insideBG);

        bTextBP.setAntiAlias(true);
        bTextBP.setColor(progressBG);

        outsideTextP.setAntiAlias(true);
        outsideTextP.setStrokeWidth(0);
        outsideTextP.setTextSize(28);
        outsideTextP.setTypeface(Typeface.DEFAULT);
        outsideTextP.setColor(outsideDefultTC);

        insideTextP.setAntiAlias(true);
        insideTextP.setStrokeWidth(0);
        insideTextP.setTextSize(28);
        insideTextP.setTypeface(Typeface.DEFAULT);
        insideTextP.setColor(outsideDefultTC);


        lintP.setAntiAlias(true);
        lintP.setStrokeWidth(progressW);
        lintP.setStyle(Paint.Style.STROKE);
        lintP.setColor(0);

        lintPbg.setAntiAlias(true);
        lintPbg.setStrokeWidth(progressW);
        lintPbg.setStyle(Paint.Style.STROKE);
        lintPbg.setColor(progressBG);

        centerP.setAntiAlias(true);
        centerP.setStrokeWidth(0);
        centerP.setTypeface(Typeface.DEFAULT);
        centerP.setTextSize(20f);
        centerP.setColor(progressBG);

        centerP1.setAntiAlias(true);
        centerP1.setStrokeWidth(0);
        centerP1.setTypeface(Typeface.DEFAULT);
        centerP1.setTextSize(40f);
        centerP1.setColor(progressBG);
    }

    private void initSelectPath(int select) {
        if (itemW == 0)
            return;
        float[] inXY = new float[]{0f, 0f};
        float[] inTan = new float[]{0f, 0f};
        measure.getPosTan(itemW * select, inXY, inTan);
        double tan = Math.atan2(inTan[1], inTan[0]) * 180.0 / Math.PI;
        int tag = 0;
        if (inTan[0] == -1) {
            tag = 1;
            if (select > outsideList.size() / 2)
                tag = 5;
        } else if (inTan[0] > 0.99 && inTan[0] < 1.01) {
            tag = 3;
        } else if (tan < 0) {
            tag = 2;
        } else {
            tag = 4;
        }
        selPath = new Path();

        selPath.moveTo(ViewW / 2, ViewH - outsideW - progressW / 2);
        selPath.lineTo(tag > 1 ? ViewH / 2 : inXY[0], ViewH - outsideW - progressW / 2);
        if (tag <= 1)
            return;
        RectF ovalLeft = new RectF(outsideW + progressW / 2, outsideW + progressW / 2, ViewH - outsideW - progressW, ViewH - outsideW - progressW / 2);
        selPath.arcTo(ovalLeft, 90, tag > 2 ? 180 : (float) (180 + tan));
        if (tag <= 2)
            return;
        selPath.lineTo(tag > 3 ? ViewW - ViewH / 2 : inXY[0], outsideW + progressW / 2);
        if (tag <= 3)
            return;
        RectF ovalRight = new RectF(ViewW - ViewH + outsideW + progressW / 2, outsideW + progressW / 2, ViewW - outsideW - progressW / 2, ViewH - outsideW - progressW / 2);
        selPath.arcTo(ovalRight, 270, tag > 4 ? 180 : (float) tan);
        if (tag <= 4)
            return;
        selPath.lineTo(inXY[0], ViewH - outsideW - progressW / 2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewW = MeasureSpec.getSize(widthMeasureSpec);
        ViewH = MeasureSpec.getSize(heightMeasureSpec);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF outsdeR = new RectF(outsideW / 2, outsideW / 2, getWidth() - outsideW / 2, getHeight() - outsideW / 2);
        RectF insideR = new RectF(outsideW + progressW, outsideW + progressW, ViewW - outsideW - progressW, ViewH - outsideW - progressW);
        initBGPath();
        canvas.drawRoundRect(outsdeR, ViewH / 2, ViewH / 2, outsideP);
        canvas.drawRoundRect(insideR, (ViewH - outsideW) / 2, (ViewH - outsideW) / 2, insideP);
        canvas.drawPath(path, lintP);
        measure = new PathMeasure();
        measure.setPath(path, true);

        String test = "这是中间的文字显示";
        canvas.drawText(test, ViewW / 2 - (String.valueOf(measure.getLength()).length() * 20f / 2), ViewH / 2, centerP);
        if (outsideList != null && outsideList.size() > 0) {
            itemW = measure.getLength() / (outsideList.size() - 1);
            for (int i = 0; i < outsideList.size(); i++) {
                float[] outXY = new float[]{0f, 0f};
                float[] outTan = new float[]{0f, 0f};
                float[] inXY = new float[]{0f, 0f};
                float[] inTan = new float[]{0f, 0f};
                measure.getPosTan(itemW * i, inXY, inTan);
                measure.getPosTan(itemW * i, outXY, outTan);
                double tan = Math.atan2(outTan[1], outTan[0]) * 180.0 / Math.PI;
                if (outTan[0] > 0.99 && outTan[0] < 1.01) {
                    outXY[1] -= 14;
                    inXY[1] += 40;
                } else if (outTan[0] == -1) {
                    outXY[1] += 40;
                    inXY[1] -= 14;
                } else if (tan < 0 && tan > -45) {
                    outXY[0] -= 5;
                    outXY[1] -= 20;
                    inXY[0] += 15;
                    inXY[1] += 40;
                } else if (tan <= -45 && tan > -90) {
                    outXY[0] -= 24;
                    outXY[1] += 10;
                    inXY[0] += 30;
                    inXY[1] += 10;
                } else if (tan <= -90 && tan > -135) {
                    outXY[0] -= 30;
                    inXY[0] += 30;
                } else if (tan <= -135 && tan > -180) {
                    outXY[0] -= 24;
                    outXY[1] += 30;
                    inXY[0] += 15;
                    inXY[1] -= 20;
                } else if (tan > 0 && tan < 45) {
                    outXY[0] += 5;
                    outXY[1] -= 15;
                    inXY[0] -= 5;
                    inXY[1] += 40;
                } else if (tan >= 45 && tan < 90) {
                    outXY[0] += 20;
                    outXY[1] -= 10;
                    inXY[0] -= 30;
                    inXY[1] += 10;
                } else if (tan >= 90 && tan < 135) {
                    outXY[0] += 30;
                    inXY[0] -= 30;
                } else if (tan >= 135 && tan < 180) {
                    outXY[0] += 24;
                    outXY[1] += 30;
                    inXY[0] -= 15;
                    inXY[1] -= 24;
                }
                if (select == i) {
                    outsideTextP.setColor(Color.WHITE);
                    canvas.drawOval(outXY[0] - 18, outXY[1] - 28, outXY[0] + 22, outXY[1] + 12, bTextBP);
                    canvas.drawText(outsideList.get(i), outXY[0] - outsideList.get(i).length() * 14 / 2, outXY[1], outsideTextP);
                } else if (select > i) {
                    outsideTextP.setColor(progressBG);
                    canvas.drawText(outsideList.get(i), outXY[0] - outsideList.get(i).length() * 14 / 2, outXY[1], outsideTextP);
                } else {
                    outsideTextP.setColor(outsideDefultTC);
                    canvas.drawText(outsideList.get(i), outXY[0] - outsideList.get(i).length() * 14 / 2, outXY[1], outsideTextP);
                }
                canvas.drawText(insideList.get(i), inXY[0] - insideList.get(i).length() * 14 / 2, inXY[1], insideTextP);
            }
            if (select != 0) {
                initSelectPath(select);
                canvas.drawPath(selPath, lintPbg);
            }
        }
        canvas.drawRoundRect(ViewW / 2 - 50, ViewH - outsideW - progressW - outsideW, ViewW / 2 + 50, ViewH, 20, 20, bTextBP);
        if (select != 0) {
            outsideTextP.setColor(Color.WHITE);
            canvas.drawText(insideList.get(select), ViewW / 2-14, ViewH - outsideW - progressW - outsideW + 64, outsideTextP);
        }
    }

    /**
     * 设置外圆文本
     *
     * @param outsideList
     */
    public void setOutsideList(List<String> outsideList) {
        this.outsideList = outsideList;
        if (outsideList != null && outsideList.size() > 0) {
            postInvalidate();
        }
    }

    /**
     * 设置内圆文本
     *
     * @param insideList
     */
    public void setInsideList(List<String> insideList) {
        this.insideList = insideList;
        if (insideList != null && insideList.size() > 0) {
            postInvalidate();
        }
    }

    /**
     * 设置当前选中的点位置
     *
     * @param select
     */
    public void setSelect(int select) {
        this.select = select;
        postInvalidate();
    }

}

