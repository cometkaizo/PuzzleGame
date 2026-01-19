package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Text;

import java.awt.*;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-15
 * Description: Screen overlay for the rosetta stone
 */
public class RosettaStoneOverlay extends Overlay {
    public static final Color COLOR = new Color(236, 228, 223);
    private final Text[] texts = {
            new Text("""
                    ÑŽ = 3
                    ÑÇ = Ceh
                    Ñä = Cumku
                    Ñë = 5
                    ÑØ = Imix
                    Ñü = Zip
                    ŽŽ = Ik'
                    ŽÇ = 6
                    Žä = Chuwen
                    Žë = Kayab
                    ŽØ = 12
                    Žü = Chen
                    ÇŽ = Lamat
                    ÇÇ = 0
                    """, Assets.font(26), COLOR,
                    w -> w / 2 - 55,
                    h -> h / 2 - 23,
                    94, false, false),
            new Text("""
                    Çä = Ajaw
                    Çë = Zotz
                    ÇØ = 2
                    Çü = Uo
                    äŽ = Xul
                    äÇ = K'ib'
                    ää = 1
                    äë = K'an
                    äØ = Muan
                    äü = 4
                    ëŽ = Muluk
                    ëÇ = Chikchan
                    ëä = Wayeb'
                    ëë = 9
                    ëØ = Mac
                    ëü = Men
                    """, Assets.font(26), COLOR,
                    w -> w / 2 - 15,
                    h -> h / 2 - 40,
                    94, false, false),
            new Text("""
                    ØÑ = 7
                    ØŽ = Ix
                    ØÇ = Mol
                    Øä = Etz'nab'
                    Øë = Kawak
                    ØØ = 10
                    Øü = Pop
                    üŽ = 8
                    üÇ = Ben
                    üä = Xul
                    üë = Yax
                    üØ = Kaban
                    üü = 11
                    """, Assets.font(26), COLOR,
                    w -> w / 2 + 24,
                    h -> h / 2 - 57,
                    94, false, false),
    };

    public RosettaStoneOverlay(GameApp app) {
        super(app);
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderCenteredImage(Assets.texture("gui/rosetta_stone"));

        for (var text : texts) text.render(canvas);
    }
}
