package com.wanbo.launcher;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Home extends Activity {
    static final int ACCENT = 0xFFFF7A00; // Laranja Wanbo
    static final int TEXT_MAIN = 0xFFF5F5F5;
    static final int TEXT_SEC = 0xFFB0B0B0;

    static class App {
        String label, pkg;
        Drawable img;
        Intent intent;
        boolean banner;
    }

    final List<App> apps = new ArrayList<>();
    BaseAdapter adapter;
    View selected;

    int dp(float v) {
        return Math.round(v * getResources().getDisplayMetrics().density);
    }

    GradientDrawable rounded(int fill, int stroke) {
        GradientDrawable g = new GradientDrawable();
        g.setCornerRadius(dp(10));
        g.setColor(fill);
        if (stroke != 0) g.setStroke(dp(3), stroke);
        return g;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(44), dp(24), dp(44), dp(16));
        root.setBackground(new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[]{0xFF1C2024, 0xFF121417, 0xFF0A0C0E}));

        // Header: Title + Clock (Left) and Shortcut Pills (Right)
        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.BOTTOM);

        LinearLayout left = new LinearLayout(this);
        left.setOrientation(LinearLayout.VERTICAL);
        header.addView(left, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        TextView title = new TextView(this);
        title.setText("WANBO T2 MAX");
        title.setTextSize(22);
        title.setTextColor(ACCENT);
        title.setTypeface(Typeface.create("sans-serif-medium", Typeface.BOLD));
        left.addView(title);

        TextClock clock = new TextClock(this);
        clock.setFormat24Hour("HH:mm");
        clock.setFormat12Hour("HH:mm");
        clock.setTextSize(52);
        clock.setTextColor(TEXT_MAIN);
        clock.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        left.addView(clock);

        TextClock date = new TextClock(this);
        date.setFormat24Hour("EEEE, d 'de' MMMM");
        date.setFormat12Hour("EEEE, d 'de' MMMM");
        date.setTextSize(16);
        date.setTextColor(TEXT_SEC);
        date.setPadding(0, 0, 0, dp(14));
        left.addView(date);

        // Shortcut Pills
        LinearLayout pills = new LinearLayout(this);
        pills.setOrientation(LinearLayout.HORIZONTAL);
        pills.setPadding(0, 0, 0, dp(14));
        header.addView(pills);

        pill(pills, "HDMI", "com.mstar.tv.tvplayer.ui/.RootActivity");
        pill(pills, "Trapézio", "com.example.zoomsettings/.MainActivity");
        pill(pills, "Configurações", "com.android.settings/.Settings");

        root.addView(header);

        // App Grid
        GridView grid = new GridView(this);
        grid.setNumColumns(GridView.AUTO_FIT);
        grid.setColumnWidth(dp(150));
        grid.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        grid.setHorizontalSpacing(dp(16));
        grid.setVerticalSpacing(dp(16));
        grid.setPadding(dp(4), dp(10), dp(4), dp(10));
        grid.setClipToPadding(false);
        grid.setSelector(new ColorDrawable(0));

        grid.setAdapter(adapter = new BaseAdapter() {
            @Override
            public int getCount() { return apps.size(); }
            @Override
            public Object getItem(int i) { return apps.get(i); }
            @Override
            public long getItemId(int i) { return i; }
            @Override
            public View getView(int i, View v, ViewGroup parent) { return tile(apps.get(i)); }
        });

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> p, View v, int i, long id) {
                try {
                    startActivity(apps.get(i).intent);
                } catch (Exception e) {
                    Toast.makeText(Home.this, "Erro ao abrir: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> p, View v, int i, long id) {
                String pkg = apps.get(i).pkg;
                if (pkg != null) {
                    Intent details = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + pkg));
                    details.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(details);
                }
                return true;
            }
        });

        grid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> p, View v, int i, long id) { select(v); }
            @Override
            public void onNothingSelected(AdapterView<?> p) { select(null); }
        });

        grid.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean has) {
                select(has ? grid.getSelectedView() : null);
            }
        });

        grid.requestFocus();
        root.addView(grid, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));

        setContentView(root);
    }

    void pill(LinearLayout row, String label, String component) {
        ComponentName cn = ComponentName.unflattenFromString(component);
        Intent i = new Intent().setComponent(cn).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (getPackageManager().resolveActivity(i, 0) == null) {
            return;
        }

        TextView p = new TextView(this);
        p.setText(label);
        p.setTextColor(TEXT_MAIN);
        p.setTextSize(14);
        p.setPadding(dp(16), dp(8), dp(16), dp(8));
        p.setFocusable(true);

        StateListDrawable bg = new StateListDrawable();
        bg.addState(new int[]{android.R.attr.state_focused}, rounded(ACCENT, 0));
        bg.addState(new int[]{}, rounded(0x33444444, 0));
        p.setBackground(bg);

        p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(i);
                } catch (Exception e) {
                    Toast.makeText(Home.this, "Erro ao abrir: " + label, Toast.LENGTH_SHORT).show();
                }
            }
        });

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = dp(10);
        row.addView(p, lp);
    }

    void select(View v) {
        if (selected != null) {
            selected.animate().scaleX(1f).scaleY(1f).setDuration(120);
            if (selected instanceof ViewGroup) {
                ((ViewGroup) selected).getChildAt(0).setForeground(null);
            }
        }
        if (v != null) {
            v.animate().scaleX(1.08f).scaleY(1.08f).setDuration(120);
            if (v instanceof ViewGroup) {
                ((ViewGroup) v).getChildAt(0).setForeground(rounded(0, ACCENT));
            }
        }
        selected = v;
    }

    View tile(App a) {
        LinearLayout t = new LinearLayout(this);
        t.setOrientation(LinearLayout.VERTICAL);

        ImageView img = new ImageView(this);
        img.setImageDrawable(a.img);
        img.setBackground(rounded(a.banner ? 0 : 0x22FFFFFF, 0));
        img.setClipToOutline(true);
        img.setScaleType(a.banner ? ImageView.ScaleType.CENTER_CROP : ImageView.ScaleType.FIT_CENTER);
        int pad = a.banner ? 0 : dp(16);
        img.setPadding(pad, pad, pad, pad);
        t.addView(img, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(86)));

        TextView label = new TextView(this);
        label.setText(a.label);
        label.setTextColor(TEXT_MAIN);
        label.setTextSize(13);
        label.setGravity(Gravity.CENTER);
        label.setSingleLine();
        label.setEllipsize(TextUtils.TruncateAt.END);
        label.setPadding(0, dp(6), 0, 0);
        t.addView(label);

        return t;
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }

    void load() {
        PackageManager pm = getPackageManager();
        Map<String, App> byPkg = new LinkedHashMap<String, App>();
        String[] categories = new String[]{Intent.CATEGORY_LEANBACK_LAUNCHER, Intent.CATEGORY_LAUNCHER};

        for (int c = 0; c < categories.length; c++) {
            String cat = categories[c];
            Intent intent = new Intent(Intent.ACTION_MAIN).addCategory(cat);
            List<ResolveInfo> list = pm.queryIntentActivities(intent, 0);
            if (list == null) continue;

            for (int j = 0; j < list.size(); j++) {
                ResolveInfo r = list.get(j);
                ActivityInfo ai = r.activityInfo;
                if (ai == null) continue;
                if (ai.packageName.equals(getPackageName()) || byPkg.containsKey(ai.packageName)) {
                    continue;
                }

                App a = new App();
                a.pkg = ai.packageName;
                a.label = r.loadLabel(pm).toString();
                Drawable banner = ai.loadBanner(pm);
                a.banner = banner != null;
                a.img = a.banner ? banner : ai.loadIcon(pm);
                a.intent = new Intent(Intent.ACTION_MAIN)
                        .addCategory(cat)
                        .setClassName(ai.packageName, ai.name)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

                byPkg.put(a.pkg, a);
            }
        }

        apps.clear();
        apps.addAll(byPkg.values());
        Collections.sort(apps, new Comparator<App>() {
            @Override
            public int compare(App x, App y) {
                return x.label.compareToIgnoreCase(y.label);
            }
        });
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        // Nada ao apertar voltar na Home
    }
}
