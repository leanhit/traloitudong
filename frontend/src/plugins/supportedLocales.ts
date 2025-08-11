const local = {
    supportedLocales: [
        {
            value: 'en',
            label: 'English (US)',
            flag: `
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 7410 3900">
          <rect width="7410" height="3900" fill="#b22234"/>
          <g fill="#fff">
            <rect y="300" width="7410" height="300"/>
            <rect y="900" width="7410" height="300"/>
            <rect y="1500" width="7410" height="300"/>
            <rect y="2100" width="7410" height="300"/>
            <rect y="2700" width="7410" height="300"/>
            <rect y="3300" width="7410" height="300"/>
          </g>
          <rect width="2964" height="2100" fill="#3c3b6e"/>
          <g fill="#fff">
            ${(() => {
                const stars = [];
                const star = (cx, cy) => `
                <polygon points="${cx},${cy - 90} ${cx + 26.4},${cy - 28.1} ${
                    cx + 85.1
                },${cy - 28.1}
                ${cx + 43.2},${cy + 10.8} ${cx + 59.4},${cy + 70.2} ${cx},${
                    cy + 35.0
                }
                ${cx - 59.4},${cy + 70.2} ${cx - 43.2},${cy + 10.8} ${
                    cx - 85.1
                },${cy - 28.1}
                ${cx - 26.4},${cy - 28.1}"/>
              `;
                for (let row = 0; row < 9; row++) {
                    const y = 105 + row * 210;
                    const offset = row % 2 === 0 ? 123 : 246;
                    for (let col = 0; col < (row % 2 === 0 ? 6 : 5); col++) {
                        stars.push(star(offset + col * 492, y));
                    }
                }
                return stars.join('');
            })()}
          </g>
        </svg>
      `,
        },
        {
            value: 'vi',
            label: 'Tiếng Việt (VN)',
            flag: `
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 480">
          <rect width="640" height="480" fill="#da251d"/>
          <polygon fill="#ff0" points="
            320,97.9
            346.5,179.5 432.3,179.5
            363,229.9 389.4,311.6
            320,261.1 250.6,311.6
            277,229.9 207.7,179.5
            293.5,179.5
          "/>
        </svg>
      `,
        },
    ],

    currencyEnum: [
        {
            label: 'USD',
            value: 'USD',
            symbol: '$',
        },
        {
            label: 'VNĐ',
            value: 'VND',
            symbol: '₫',
        },
        // {
        //     label: 'Euro',
        //     value: 'EUR',
        //     symbol: '€',
        // },
    ],

    localCurrency: localStorage.getItem('appCurrency') || 'USD',
    currentLanguage: localStorage.getItem('appLocale') || 'en',
    taxPercent: 10.5,
};

export default local;