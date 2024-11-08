package tactical.blue.parsing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UrlSearchQueryNormalizerTest {

    @Test
    public void testNormalizeSearchQuery() {
        // Test cases based on the item descriptions provided

        assertEquals("test strep a clia waived rapid strep test",
                UrlSearchQueryNormalizer.normalizeSearchQuery("https://boundtree.com/search/?text=test+strep+a+-+clia+waived+rapid+strep+test"));

        assertEquals("kit test status influenza a b clia wvd",
                UrlSearchQueryNormalizer.normalizeSearchQuery("https://boundtree.com/search/?text=kit+test+-+status+influenza+a+%26+b+clia+wvd"));

        assertEquals("ph paper in dispenser hydrion insta chek 0 to 13.0",
                UrlSearchQueryNormalizer.normalizeSearchQuery("https://boundtree.com/search/?text=ph+paper+in+dispenser+hydrion%C2%AE+insta-chek%C2%AE+0+to+13.0"));

        assertEquals("test drug rapid 12 panel",
                UrlSearchQueryNormalizer.normalizeSearchQuery("https://boundtree.com/search/?text=test+drug+-+rapid+12+panel"));

        assertEquals("kit test hemoccult ifob 2 hole",
                UrlSearchQueryNormalizer.normalizeSearchQuery("https://boundtree.com/search/?text=kit+test+-+hemoccult+ifob+2-hole"));

        assertEquals("kit rapid fobt test stool clia waived 30sec 1min",
                UrlSearchQueryNormalizer.normalizeSearchQuery("https://boundtree.com/search/?text=kit+rapid+fobt+test+-+stool+clia+waived+30sec+-+1min"));

        assertEquals("control urinalysis dipper poct lvl 1 2 1 5ml",
                UrlSearchQueryNormalizer.normalizeSearchQuery("https://boundtree.com/search/?text=control+urinalysis+-+dipper+poct+lvl+1+%26+2+1.5ml"));

        assertEquals("urine chemistry control set dropper plus urinalysis dipstick testing 2 levels 10 x 5 ml",
                UrlSearchQueryNormalizer.normalizeSearchQuery("https://boundtree.com/search/?text=urine+chemistry+control+set+dropper%C2%AE+plus+urinalysis+dipstick+testing+2+levels+10+x+5+ml"));

        assertEquals("urine chemistry control set quantimetrix dipper poct 2 levels 20 x 1 5 ml control urine dipper poct dipstick bi level 20 bx",
                UrlSearchQueryNormalizer.normalizeSearchQuery("https://boundtree.com/search/?text=urine+chemistry+control+set+quantimetrix+dipper+poct+2+levels+20+x+1.5+ml%3Bcontrol+urine+dipper+poct+dipstick+bi-level+%2820%2Fbx%29"));

        assertEquals("urine chemistry control set dipper urinalysis dipstick testing 2 levels 6 x 15 ml",
                UrlSearchQueryNormalizer.normalizeSearchQuery("https://boundtree.com/search/?text=urine+chemistry+control+set+dipper%C2%AE+urinalysis+dipstick+testing+2+levels+6+x+15+ml"));

        assertEquals("control urine dipper lev 1 2 15ml",
                UrlSearchQueryNormalizer.normalizeSearchQuery("https://boundtree.com/search/?text=control+urine+dipper+lev+1%262+15ml"));

        assertEquals("ot strip 10sg urinalysis test multistix clia waived",
                UrlSearchQueryNormalizer.normalizeSearchQuery("https://boundtree.com/search/?text=ot%3A+strip+10sg+urinalysis+test+-+multistix+clia+waived"));

        assertEquals("strip parameter urinalysis test clinistrip 11",
                UrlSearchQueryNormalizer.normalizeSearchQuery("https://boundtree.com/search/?text=strip+parameter+urinalysis+test+-+clinistrip-11"));

        assertEquals("analyzer urinalysis clinitek status",
                UrlSearchQueryNormalizer.normalizeSearchQuery("https://boundtree.com/search/?text=analyzer+urinalysis+-+clinitek+status+%2B"));

        assertEquals("power supply kit for cliniteck urinalysis",
                UrlSearchQueryNormalizer.normalizeSearchQuery("https://boundtree.com/search/?text=power+supply+kit+for+cliniteck+urinalysis"));

        assertEquals("paper thermal clinitek",
                UrlSearchQueryNormalizer.normalizeSearchQuery("https://boundtree.com/search/?text=paper+thermal+clinitek"));

        assertEquals("kit hcg pregnancy test osom clia waived",
                UrlSearchQueryNormalizer.normalizeSearchQuery("https://boundtree.com/search/?text=kit+hcg+pregnancy+test+-+osom+clia+waived"));

        assertEquals("control hcg quickvue one step pos neg",
                UrlSearchQueryNormalizer.normalizeSearchQuery("https://boundtree.com/search/?text=control+hcg+-+quickvue+one-step+pos%2Fneg"));

        assertEquals("test monofilament sensory foot 10gm 20 pack",
                UrlSearchQueryNormalizer.normalizeSearchQuery("https://boundtree.com/search/?text=test+monofilament+-+sensory+foot+10gm%2F20+pack"));

        assertEquals("monofilament baseline",
                UrlSearchQueryNormalizer.normalizeSearchQuery("https://boundtree.com/search/?text=monofilament+baseline"));

        assertEquals("rack test tube 16 20mm 32 place clr",
                UrlSearchQueryNormalizer.normalizeSearchQuery("https://boundtree.com/search/?text=rack+test+tube+-+16-20mm+32+place+clr"));
    }
}