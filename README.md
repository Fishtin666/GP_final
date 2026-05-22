這是一個結合 虛擬實境 (VR) 與 行動應用程式 (APP) 的全方位雅思（IELTS）學習系統。透過 AI 技術輔助，打破時間與空間的限制，讓學習者隨時隨地都能進行高擬真的雅思口說、聽力與寫作練習。

一、專案亮點與核心特色:
1. AI 虛擬考官互動：整合進階 AI SDK，模擬真實雅思考場的口說對話與流程。
2. 精準 AI 評語與回饋：口說與寫作練習後，系統會針對語法、內容完整度給予詳細的雙語評語，並提供高分範例解答。
3. 多功能智能輔助：內建 Azure 發音評定、點擊單字即查字典、AI 生成範例回答等功能。

二、系統架構與功能模組
本系統主要分為兩大核心子系統：
1.  虛擬實境系統 (VR System)<br>
    a. 虛擬考場模擬：完整模擬雅思口說真實考試場景，幫助考生熟悉考試流程，消除面對真人考官的緊張感。

    b. 動態虛擬小鎮：與虛擬 AI NPC 進行互動，模擬日常生活的真實情境，全方位鍛鍊雅思口說與聽力能力。

2. 行動應用程式 (APP System)<br>
提供四大核心功能分頁（學習、AI老師、單字、複習），包含：<br>
  **學習<br>**
    a. 聽說讀寫四大練習：全方位的題目演練，支援寫作學習與口說學習。<br>
    b. 發音評定功能：針對發音給予精確的評定分數（如準確度、流利度、完整度），並用顏色標示發音不正確的單字。<br>

   **AI 老師與對話：<br>**
      a. 專題/與AI對話：勾選題目即可與 AI 對話、生成範例回答；點擊單字直接跳出字典視窗。<br>
      b. 智慧燈泡提示：卡關時點擊燈泡，AI 會給予提示（例如：若要讓分數超過 70 分，建議使用的核心句子）。<br>

   **多元複習與字典**：分類明確的單字測驗與精準字典，方便隨時複習。
<table align="center">
  <tr>    
    <td align="center" width="33%"><b>學習頁面</b></td>
    <td align="center" width="33%"><b>AI 生成範例回答與發音評定</b></td>
    <td align="center" width="33%"><b>AI 智慧評語與回饋</b></td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/3cc4052f-ecb9-4e56-b9fb-2bd741e2d29b" width="100%"/></td>
    <td><img src="https://github.com/user-attachments/assets/e4df83ba-0453-4915-87bf-e7bd69dd5efe" width="100%"/></td>
    <td><img src="https://github.com/user-attachments/assets/7f268872-0579-4cae-b69c-94fdd1a07642" width="100%"/></td>
  </tr>
</table>

三、使用技術 
1. 前端 / 行動端 (APP)：Android Studio (Java)

2. 資料庫 ：firebase

3. 人工智慧與雲端 API (AI & Cloud Services)：

4. Microsoft Azure Speech Service：用於提供精準的發音評定分數。

5. LLM API (OpenAI)：用於生成客製化的 AI 口說/寫作雙語詳細評語與範例回答。

6. 虛擬實境 (VR)：Unity
