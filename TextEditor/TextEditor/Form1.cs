using System;
using System.IO;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Collections.Generic;

namespace TextEditor
{
    public partial class Form1 : Form

    {
        // Поле для хранения истории файлов
        private List<string> recentFiles = new List<string>();
        private Timer autoSaveTimer = new Timer();
        private string historyFolder = Path.Combine(Application.StartupPath, "History");

        public Form1()
        {
            InitializeComponent();
            InitializeAutoSave(); // Включаем автосохранение
        }

        private void InitializeAutoSave()
        {
            if (string.IsNullOrWhiteSpace(historyFolder) || !Directory.Exists(historyFolder))
            {
                Directory.CreateDirectory(historyFolder);
            }

            if (!autoSaveTimer.Enabled) // Предотвращаем повторный запуск
            {
                autoSaveTimer.Interval = 30000; // 30 секунд
                autoSaveTimer.Tick += AutoSave_Tick;
                autoSaveTimer.Enabled = true;
                autoSaveTimer.Start();
            }
        }

        private void AutoSave_Tick(object sender, EventArgs e)
        {
            if (tabControl1.SelectedTab == null) return;

            RichTextBox richTextBox = tabControl1.SelectedTab.Controls.OfType<RichTextBox>().FirstOrDefault();
            if (richTextBox == null || string.IsNullOrWhiteSpace(richTextBox.Text)) return;

            // Очищаем имя файла от недопустимых символов
            string fileName = tabControl1.SelectedTab.Text;
            if (string.IsNullOrWhiteSpace(fileName)) fileName = "Untitled";
            fileName = string.Concat(fileName.Split(Path.GetInvalidFileNameChars()));

            // Создаём имя файла с датой
            string timestamp = DateTime.Now.ToString("yyyyMMdd_HHmmss");
            string savePath = Path.Combine(historyFolder, $"{fileName}_{timestamp}.txt");

            try
            {
                File.WriteAllText(savePath, richTextBox.Text);

                // Ограничиваем число файлов в папке (оставляем последние 10)
                string[] savedFiles = Directory.GetFiles(historyFolder, $"{fileName}_*.txt")
                                               .OrderByDescending(f => f)
                                               .Skip(10) // Удаляем всё, кроме 10 последних
                                               .ToArray();

                foreach (string oldFile in savedFiles)
                {
                    File.Delete(oldFile);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Ошибка автосохранения: {ex.Message}", "Ошибка", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        private void UpdateRecentFiles(string filePath)
        {
            // Удаляем файл из списка, если он уже есть (чтобы не дублировался)
            recentFiles.Remove(filePath);

            // Добавляем в начало списка
            recentFiles.Insert(0, filePath);

            // Ограничиваем список 5 элементами
            if (recentFiles.Count > 5)
            {
                recentFiles.RemoveAt(5);
            }
        }

        private void создатьToolStripMenuItem_Click(object sender, EventArgs e)
        {
            // Создаем новую вкладку с названием
            string title = "TabPage " + (tabControl1.TabCount + 1).ToString();
            TabPage newTabPage = new TabPage(title);

            // Создаем новый RichTextBox
            RichTextBox newRichTextBox = new RichTextBox
            {
                Name = "richTextBox" + tabControl1.TabCount, // Даем уникальное имя
                Dock = DockStyle.Fill // Заполняет всю вкладку
            };

            // Добавляем RichTextBox на вкладку
            newTabPage.Controls.Add(newRichTextBox);

            // Добавляем новую вкладку в TabControl
            tabControl1.TabPages.Add(newTabPage);
        }

        private void закрытьToolStripMenuItem_Click(object sender, EventArgs e)
        {
            if (tabControl1.TabPages.Count > 0)
            {
                // Проверяем, есть ли RichTextBox на текущей вкладке
                RichTextBox richTextBox = tabControl1.SelectedTab.Controls.OfType<RichTextBox>().FirstOrDefault();

                if (richTextBox != null && !string.IsNullOrEmpty(richTextBox.Text))
                {
                    // Спрашиваем пользователя, хочет ли он сохранить файл перед закрытием
                    DialogResult result = MessageBox.Show(
                        "Сохранить изменения перед закрытием?",
                        "Сохранение",
                        MessageBoxButtons.YesNoCancel,
                        MessageBoxIcon.Warning);

                    if (result == DialogResult.Cancel)
                    {
                        return; // Отмена закрытия
                    }
                    else if (result == DialogResult.Yes)
                    {
                        // Вызываем метод сохранения
                        сохранитьToolStripMenuItem_Click(sender, e);
                    }
                }

                // Закрываем текущую вкладку
                tabControl1.TabPages.Remove(tabControl1.SelectedTab);
            }
        }

        private void richTextBox1_TextChanged(object sender, EventArgs e)
        {

        }

        private void сохранитьToolStripMenuItem_Click(object sender, EventArgs e)
        {
            if (tabControl1.SelectedTab != null)
            {
                RichTextBox richTextBox = tabControl1.SelectedTab.Controls.OfType<RichTextBox>().FirstOrDefault();

                if (richTextBox != null)
                {
                    SaveFileDialog saveFileDialog = new SaveFileDialog
                    {
                        Filter = "Текстовые файлы (*.txt)|*.txt|Все файлы (*.*)|*.*",
                        Title = "Сохранить как",
                        DefaultExt = "txt"
                    };

                    if (saveFileDialog.ShowDialog() == DialogResult.OK)
                    {
                        File.WriteAllText(saveFileDialog.FileName, richTextBox.Text);
                        tabControl1.SelectedTab.Text = Path.GetFileName(saveFileDialog.FileName);

                        // Добавляем файл в историю
                        UpdateRecentFiles(saveFileDialog.FileName);

                        MessageBox.Show("Файл успешно сохранен!", "Сохранение", MessageBoxButtons.OK, MessageBoxIcon.Information);
                    }
                }
                else
                {
                    MessageBox.Show("RichTextBox не найден на текущей вкладке!", "Ошибка", MessageBoxButtons.OK, MessageBoxIcon.Error);
                }
            }
            else
            {
                MessageBox.Show("Нет открытых вкладок!", "Ошибка", MessageBoxButtons.OK, MessageBoxIcon.Warning);
            }
        }

        private void открытьToolStripMenuItem_Click(object sender, EventArgs e)
        {
            OpenFileDialog openFileDialog = new OpenFileDialog
            {
                Filter = "Текстовые файлы (*.txt)|*.txt|Все файлы (*.*)|*.*",
                Title = "Открыть файл"
            };

            if (openFileDialog.ShowDialog() == DialogResult.OK)
            {
                string fileContent = File.ReadAllText(openFileDialog.FileName);
                string fileName = Path.GetFileName(openFileDialog.FileName);

                TabPage newTabPage = new TabPage(fileName);
                RichTextBox richTextBox = new RichTextBox
                {
                    Dock = DockStyle.Fill,
                    Text = fileContent
                };

                newTabPage.Controls.Add(richTextBox);
                tabControl1.TabPages.Add(newTabPage);
                tabControl1.SelectedTab = newTabPage;

                // Добавляем файл в историю
                UpdateRecentFiles(openFileDialog.FileName);
            }
        }

        private void историяToolStripMenuItem_Click(object sender, EventArgs e)
        {

            if (recentFiles.Count > 0)
            {
                string history = "Последние файлы:\n" + string.Join("\n", recentFiles);
                MessageBox.Show(history, "История", MessageBoxButtons.OK, MessageBoxIcon.Information);
            }
            else
            {
                MessageBox.Show("История пуста!", "История", MessageBoxButtons.OK, MessageBoxIcon.Information);
            }

        }

        private void восстановитьВерсиюToolStripMenuItem_Click(object sender, EventArgs e)
        {
            // Проверяем, задан ли historyFolder и существует ли папка, если нет – создаём
            if (string.IsNullOrWhiteSpace(historyFolder))
            {
                MessageBox.Show("Ошибка: Папка истории не задана!", "Ошибка", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return;
            }

            if (!Directory.Exists(historyFolder))
            {
                Directory.CreateDirectory(historyFolder);
            }

            if (tabControl1.SelectedTab == null)
            {
                MessageBox.Show("Ошибка: Не выбрана вкладка!", "Ошибка", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                return;
            }

            string fileName = tabControl1.SelectedTab.Text;
            if (string.IsNullOrWhiteSpace(fileName)) fileName = "Untitled";

            string[] vers = Directory.GetFiles(historyFolder, $"{fileName}_*.txt");

            if (vers.Length == 0)
            {
                MessageBox.Show("Нет сохранённых версий!", "История", MessageBoxButtons.OK, MessageBoxIcon.Information);
                return;
            }

            // Сортировка по номеру версии (если после "_" идёт число)
            string selectedFile = vers
                .OrderByDescending(f => int.TryParse(Path.GetFileNameWithoutExtension(f)
                                                    .Split('_').Last(), out int num) ? num : 0)
                .First();

            try
            {
                string content = File.ReadAllText(selectedFile);

                // Ищем RichTextBox во вкладке
                RichTextBox richTextBox = tabControl1.SelectedTab.Controls.OfType<RichTextBox>().FirstOrDefault();
                if (richTextBox == null)
                {
                    MessageBox.Show("Ошибка: Вкладка не содержит текстового поля!", "Ошибка", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    return;
                }

                richTextBox.Text = content;
                MessageBox.Show($"Восстановлено из: {Path.GetFileName(selectedFile)}", "Восстановление", MessageBoxButtons.OK, MessageBoxIcon.Information);
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Ошибка при чтении файла: {ex.Message}", "Ошибка", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }
    }
}
