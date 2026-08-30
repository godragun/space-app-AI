/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  darkMode: "class",
  theme: {
    extend: {
      "colors": {
        "on-secondary-container": "#a9bad3",
        "inverse-on-surface": "#2e3037",
        "primary": "#ffffff",
        "on-secondary-fixed": "#0b1c30",
        "outline-variant": "#444748",
        "on-tertiary": "#2f3131",
        "on-tertiary-fixed-variant": "#454747",
        "on-secondary": "#213145",
        "secondary-fixed-dim": "#b7c8e1",
        "on-surface": "#e1e2eb",
        "surface-container-low": "#191c22",
        "on-primary-fixed-variant": "#454747",
        "surface-dim": "#10131a",
        "surface-tint": "#c6c6c7",
        "on-tertiary-container": "#636565",
        "tertiary-fixed-dim": "#c6c6c7",
        "surface-container-lowest": "#0b0e14",
        "on-error": "#690005",
        "on-primary-fixed": "#1a1c1c",
        "on-primary-container": "#636565",
        "primary-fixed": "#e2e2e2",
        "tertiary": "#ffffff",
        "error-container": "#93000a",
        "background": "#10131a",
        "surface-bright": "#363940",
        "secondary": "#b7c8e1",
        "primary-container": "#e2e2e2",
        "secondary-container": "#3a4a5f",
        "primary-fixed-dim": "#c6c6c7",
        "tertiary-container": "#e2e2e2",
        "surface-container-highest": "#32353c",
        "on-background": "#e1e2eb",
        "on-surface-variant": "#c4c7c8",
        "surface": "#10131a",
        "inverse-surface": "#e1e2eb",
        "surface-container-high": "#272a31",
        "on-primary": "#2f3131",
        "tertiary-fixed": "#e2e2e2",
        "surface-container": "#1d2026",
        "on-tertiary-fixed": "#1a1c1c",
        "error": "#ffb4ab",
        "on-secondary-fixed-variant": "#38485d",
        "inverse-primary": "#5d5f5f",
        "outline": "#8e9192",
        "surface-variant": "#32353c",
        "on-error-container": "#ffdad6",
        "secondary-fixed": "#d3e4fe"
      },
      "borderRadius": {
        "DEFAULT": "0.25rem",
        "lg": "0.5rem",
        "xl": "0.75rem",
        "full": "9999px"
      },
      "spacing": {
        "margin-mobile": "16px",
        "container-max": "1200px",
        "unit": "4px",
        "gutter": "16px",
        "margin-tablet": "32px"
      },
      "fontFamily": {
        "display-lg": ["Inter", "sans-serif"],
        "label-caps": ["JetBrains Mono", "monospace"],
        "data-md": ["JetBrains Mono", "monospace"],
        "data-lg": ["JetBrains Mono", "monospace"],
        "headline-md": ["Inter", "sans-serif"],
        "body-base": ["Inter", "sans-serif"],
        "display-lg-mobile": ["Inter", "sans-serif"]
      },
      "fontSize": {
        "display-lg": ["32px", { "lineHeight": "1.2", "letterSpacing": "-0.02em", "fontWeight": "700" }],
        "label-caps": ["12px", { "lineHeight": "1", "letterSpacing": "0.1em", "fontWeight": "700" }],
        "data-md": ["16px", { "lineHeight": "1.2", "fontWeight": "500" }],
        "data-lg": ["24px", { "lineHeight": "1.1", "fontWeight": "700" }],
        "headline-md": ["20px", { "lineHeight": "1.4", "letterSpacing": "0.02em", "fontWeight": "600" }],
        "body-base": ["16px", { "lineHeight": "1.5", "fontWeight": "400" }],
        "display-lg-mobile": ["24px", { "lineHeight": "1.2", "fontWeight": "700" }]
      }
    }
  },
  plugins: [],
}
