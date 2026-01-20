package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Text;

import java.awt.*;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Screen overlay for the rosetta stone
 */
public class RosettaStoneOverlay extends Overlay {
    public static final Color COLOR = new Color(236, 228, 223);
    private final Text[] texts = {
            new Text("""
                    Žë = Kayab
                    ŽØ = 12
                    ÑŽ = 3
                    üØ = Kaban
                    Øä = Etz'nab'
                    Ñë = 5
                    Øë = Kawak
                    Žü = Chen
                    ŽÇ = 6
                    ëŽ = Muluk
                    üë = Yax
                    Çë = Zotz
                    ÇØ = 2
                    ŽŽ = Ik'
                    """, Assets.font(26), COLOR,
                    w -> w / 2 - 55,
                    h -> h / 2 - 23,
                    94, false, false),
            new Text("""
                    Çä = Ajaw
                    ëØ = Mac
                    ÇŽ = Lamat
                    ÑÇ = Ceh
                    ëÇ = Chikchan
                    üŽ = 8
                    üÇ = Ben
                    ää = 1
                    üä = Xul
                    ØØ = 10
                    Ñä = Cumku
                    ëä = Wayeb'
                    ëü = Men
                    ëë = 9
                    äÇ = K'ib'
                    äë = K'an
                    """, Assets.font(26), COLOR,
                    w -> w / 2 - 15,
                    h -> h / 2 - 40,
                    94, false, false),
            new Text("""
                    ØÑ = 7
                    Žä = Chuwen
                    ÇÇ = 0
                    ØÇ = Mol
                    ÑØ = Imix
                    äŽ = Xul
                    Ñü = Zip
                    Øü = Pop
                    üü = 11
                    Çü = Uo
                    äØ = Muan
                    äü = 4
                    ØŽ = Ix
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
