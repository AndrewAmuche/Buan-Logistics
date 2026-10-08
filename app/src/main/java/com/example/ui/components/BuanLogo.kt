package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BuanBlueCta
import com.example.ui.theme.BuanBlueLight
import com.example.ui.theme.BuanBluePrimary
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

/**
 * Official Buan Business Logo Component:
 * Faithfully matches the official branding:
 * - Bold royal blue wordmark "Buan"
 * - The iconic circular radar beacon with outward quadrant sector scan atop 'a'
 * - Horizontal underline rule
 * - Tagline: "Pay Less For More..."
 */
@Composable
fun BuanOfficialBusinessLogo(
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 28.sp,
    taglineSize: TextUnit = 11.sp,
    showTagline: Boolean = true,
    useWhiteCard: Boolean = false,
    primaryColor: Color = Color(0xFF0038E0) // Royal Blue of the official brand
) {
    val logoContent = @Composable {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            // "Buan" Wordmark with Radar Antenna Symbol
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ) {
                // "Bu"
                Text(
                    text = "Bu",
                    fontSize = fontSize,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.SansSerif,
                    color = primaryColor,
                    letterSpacing = (-0.5).sp
                )

                // "a" with official radar antenna dish mounted above
                Box(
                    contentAlignment = Alignment.BottomCenter
                ) {
                    // Antenna Radar Beacon Icon above the 'a'
                    Canvas(
                        modifier = Modifier
                            .size(width = (fontSize.value * 0.72).dp, height = (fontSize.value * 0.78).dp)
                            .align(Alignment.TopEnd)
                            .padding(bottom = (fontSize.value * 0.28).dp, start = (fontSize.value * 0.12).dp)
                    ) {
                        val w = size.width
                        val h = size.height
                        val pivot = Offset(w * 0.25f, h * 0.85f)

                        // 1. Sector Arc / Dish (Pie slice from pivot upwards-right)
                        val dishPath = Path().apply {
                            moveTo(pivot.x, pivot.y)
                            arcTo(
                                rect = Rect(
                                    left = pivot.x - w * 0.75f,
                                    top = pivot.y - h * 0.85f,
                                    right = pivot.x + w * 0.75f,
                                    bottom = pivot.y + h * 0.85f
                                ),
                                startAngleDegrees = 270f,
                                sweepAngleDegrees = 65f,
                                forceMoveTo = false
                            )
                            close()
                        }
                        drawPath(dishPath, color = primaryColor)

                        // 2. White highlight beam inside the radar dish
                        drawLine(
                            color = Color.White,
                            start = pivot,
                            end = Offset(pivot.x + w * 0.42f, pivot.y - h * 0.65f),
                            strokeWidth = 2.5f,
                            cap = StrokeCap.Round
                        )

                        // 3. Pivot Circle & Ring
                        drawCircle(
                            color = primaryColor,
                            radius = 6.5f,
                            center = pivot
                        )
                        drawCircle(
                            color = Color.White,
                            radius = 2.5f,
                            center = pivot
                        )
                    }

                    // Lowercase 'a'
                    Text(
                        text = "a",
                        fontSize = fontSize,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.SansSerif,
                        color = primaryColor,
                        letterSpacing = (-0.5).sp
                    )
                }

                // "n"
                Text(
                    text = "n",
                    fontSize = fontSize,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.SansSerif,
                    color = primaryColor,
                    letterSpacing = (-0.5).sp
                )
            }

            // Solid horizontal underline bar beneath wordmark
            Spacer(modifier = Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .width((fontSize.value * 3.6).dp)
                    .height(2.5.dp)
                    .clip(RoundedCornerShape(1.dp))
                    .background(primaryColor)
            )

            // Slogan: "Pay Less For More..."
            if (showTagline) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Pay Less For More...",
                    fontSize = taglineSize,
                    fontWeight = FontWeight.Medium,
                    fontStyle = FontStyle.Normal,
                    color = primaryColor.copy(alpha = 0.88f),
                    letterSpacing = 0.5.sp
                )
            }
        }
    }

    if (useWhiteCard) {
        Surface(
            modifier = modifier.shadow(6.dp, RoundedCornerShape(16.dp)),
            color = Color.White,
            shape = RoundedCornerShape(16.dp)
        ) {
            logoContent()
        }
    } else {
        Box(modifier = modifier) {
            logoContent()
        }
    }
}

/**
 * Compact Buan Official Logo Badge for TopBars and app navigation headers
 */
@Composable
fun BuanOfficialLogoBadge(
    modifier: Modifier = Modifier,
    height: Dp = 38.dp,
    onDark: Boolean = true
) {
    Surface(
        modifier = modifier
            .height(height)
            .shadow(4.dp, RoundedCornerShape(10.dp)),
        color = if (onDark) Color(0xFF0F172A) else Color.White,
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (onDark) Color(0xFF1E3A8A) else Color(0xFFE2E8F0)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BuanOfficialBusinessLogo(
                fontSize = 18.sp,
                taglineSize = 7.sp,
                showTagline = true,
                primaryColor = if (onDark) Color(0xFF3B82F6) else Color(0xFF0038E0)
            )
        }
    }
}
