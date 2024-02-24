package com.app.calorietracker.ui.stats.charting;

import android.graphics.Canvas;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class MultiLineYAxisRenderer extends YAxisRenderer {
    public MultiLineYAxisRenderer(ViewPortHandler viewPortHandler, YAxis yAxis, Transformer trans) {
        super(viewPortHandler, yAxis, trans);
    }
    
    @Override
    protected void drawYLabels(Canvas c, float fixedPosition, float[] positions, float offset) {
        final int from = mYAxis.isDrawBottomYLabelEntryEnabled() ? 0 : 1;
        final int to = mYAxis.isDrawTopYLabelEntryEnabled()
                ? mYAxis.mEntryCount
                : (mYAxis.mEntryCount - 1);
        
        // draw
        for (int i = from; i < to; i++) {
            
            String formattedLabel = mYAxis.getFormattedLabel(i);
            String[] line = formattedLabel.split("\n");
            
            c.drawText(line[0], fixedPosition, positions[i * 2 + 1] + offset, mAxisLabelPaint);
            for (int j = 1; j < line.length; j++) {
                float y = positions[i * 2 + 1] + offset + mAxisLabelPaint.getTextSize() * j;
                c.drawText(line[j], fixedPosition, y, mAxisLabelPaint);
            }
        }
    }
}
