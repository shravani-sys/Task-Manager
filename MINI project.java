package mp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

class Task {
	public Task next;
	String taskName;
	String description;
	String priority;
	String category;
	Date dueDate;
	boolean isCompleted;
	Task left;
	Task right;

	public Task(String taskName, String description, String priority, String category, Date dueDate) {
		this.taskName = taskName;
		this.description = description;
		this.priority = priority;
		this.category = category;
		this.dueDate = dueDate;
		this.isCompleted = false;
		this.left = null;
		this.right = null;
		this.next = null;
	}

	public String getFormattedDueDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		return sdf.format(dueDate);
	}
}

class CustomLinkedList {
	Task head;

	public CustomLinkedList() {
		head = null;
	}

	public void addToEnd(String taskName) {
		Task newTask = new Task(taskName, "", "", "", null);

		if (head == null) {
			head = newTask;
		} else {
			Task current = head;
			while (current.next != null) {
				current = current.next;
			}
			current.next = newTask;
		}
	}

	public void displayCompletedTasks() {
		Task current = head;
		System.out.println("Completed Tasks:");
		while (current != null) {
			System.out.println(current.taskName);
			current = current.next;
		}
	}

	public boolean containsTask(String taskName) {
		Task current = head;
		while (current != null) {
			if (current.taskName.equals(taskName)) {
				return true;
			}
			current = current.next;
		}
		return false;
	}

	public void deleteTaskByName(String taskName) {
		if (head == null) {
			return;
		}

		if (head.taskName.equals(taskName)) {
			head = head.next;
			return;
		}

		Task current = head;
		while (current.next != null) {
			if (current.next.taskName.equals(taskName)) {
				current.next = current.next.next;
				return;
			}
			current = current.next;
		}
	}

}

class TaskManager {
	Task root;
	CustomLinkedList completedTasks;
	CustomLinkedList personalTasks; // Linked list for personal tasks
	CustomLinkedList workTasks; // Linked list for work tasks
	CustomLinkedList healthTasks; // Linked list for health tasks

	public TaskManager() {
		root = null;
		completedTasks = new CustomLinkedList();
		personalTasks = new CustomLinkedList();
		workTasks = new CustomLinkedList();
		healthTasks = new CustomLinkedList();
	}

	public void addTask(String taskName, String description, String priority, String category, Date dueDate) {
		Task newTask = new Task(taskName, description, priority, category, dueDate);
		root = insertTask(root, newTask);
		// Add the task to the appropriate linked list based on the category
		switch (category.toLowerCase()) {
			case "personal":
				personalTasks.addToEnd(taskName);
				break;
			case "work":
				workTasks.addToEnd(taskName);
				break;
			case "health":
				healthTasks.addToEnd(taskName);
				break;
			default:
				System.out.println("Invalid category");
				break;
		}
	}

	// Display tasks by category
	public void displayTasksByCategory(String category) {
		CustomLinkedList listToDisplay = null;

		switch (category.toLowerCase()) {
			case "personal":
				listToDisplay = personalTasks;
				break;
			case "work":
				listToDisplay = workTasks;
				break;
			case "health":
				listToDisplay = healthTasks;
				break;
			default:
				System.out.println("Invalid category.");
				return;
		}

		if (listToDisplay != null) {
			Task current = listToDisplay.head;
			System.out.println(category + " Tasks:");
			while (current != null) {
				System.out.println(current.taskName);
				current = current.next;
			}
		}
	}

	private Task insertTask(Task root, Task newTask) {
		if (root == null) {
			return newTask;
		}

		int priorityComparison = newTask.priority.compareTo(root.priority);

		// If priority is the same, order by task name
		if (priorityComparison == 0) {
			int nameComparison = newTask.taskName.compareTo(root.taskName);
			if (nameComparison < 0) {
				root.left = insertTask(root.left, newTask);
			} else {
				root.right = insertTask(root.right, newTask);
			}
		} else if (newTask.priority.equalsIgnoreCase("urgent")) {
			root.left = insertTask(root.left, newTask);
		} else if (newTask.priority.equalsIgnoreCase("upcoming")) {
			root.right = insertTask(root.right, newTask);
		}

		return root;
	}

	public void displayUrgentTasks() {
		System.out.println("Urgent Tasks:");
		if (root.priority.equalsIgnoreCase("urgent")) {
			System.out.println("Task Name: " + root.taskName);
			System.out.println("Description: " + root.description);
			System.out.println("Priority: " + root.priority);
			System.out.println("Category: " + root.category);
			System.out.println();
		}
		displayTasks(root.left);

	}

	public void displayUpcomingTasks() {
		System.out.println("Upcoming Tasks:");
		if (root.priority.equalsIgnoreCase("upcoming")) {
			System.out.println("Task Name: " + root.taskName);
			System.out.println("Description: " + root.description);
			System.out.println("Priority: " + root.priority);
			System.out.println("Category: " + root.category);
			System.out.println();
		}
		displayTasks(root.right);
	}

	private void displayTasks(Task root) {
		if (root != null) {
			displayTasks(root.left);
			System.out.println("Task Name: " + root.taskName);
			System.out.println("Description: " + root.description);
			System.out.println("Priority: " + root.priority);
			System.out.println("Category: " + root.category);
			System.out.println();
			displayTasks(root.right);
		}
	}

	public void removeTask(String taskName) {
		root = removeTask(root, taskName);
		System.out.println("Task removed successfully");
	}

	private Task removeTask(Task root, String taskName) {
		if (root == null) {
			return root;
		}

		int compareResult = taskName.compareTo(root.taskName);

		if (compareResult < 0) {
			root.left = removeTask(root.left, taskName);
		} else if (compareResult > 0) {
			root.right = removeTask(root.right, taskName);
		} else {
			if (root.left == null && root.right == null) {
				return null;
			} else if (root.left == null) {
				return root.right;
			} else if (root.right == null) {
				return root.left;
			}
			root.taskName = minValue(root.right);
			root.right = removeTask(root.right, root.taskName);

		}

		return root;

	}

	private String minValue(Task root) {
		String minValue = root.taskName;
		while (root.left != null) {
			minValue = root.left.taskName;
			root = root.left;
		}
		return minValue;
	}

	private void markTaskAsCompleted(Task root, String taskName) {
		if (root == null) {
			return;
		}

		int comparison = taskName.compareTo(root.taskName);

		if (comparison == 0) {
			System.out.println("Task marked as completed");
			root.isCompleted = true;
			completedTasks.addToEnd(root.taskName);
		} else if (comparison < 0) {
			markTaskAsCompleted(root.left, taskName);

		} else {
			markTaskAsCompleted(root.right, taskName);
		}
	}

	public void markTaskAsCompleted(String taskName) {
		markTaskAsCompleted(root, taskName);

	}

	public void editTask(String taskName, String newTaskName, String newDescription) {
		root = editTaskRec(root, taskName, newTaskName, newDescription);
	}

	private Task editTaskRec(Task root, String taskName, String newTaskName, String newDescription) {
		if (root == null) {
			return null;
		}

		int comparison = taskName.compareTo(root.taskName);

		if (comparison == 0) {
			root.taskName = newTaskName;
			root.description = newDescription;
		} else if (comparison < 0) {
			root.left = editTaskRec(root.left, taskName, newTaskName, newDescription);
		} else {
			root.right = editTaskRec(root.right, taskName, newTaskName, newDescription);
		}

		return root;
	}

	public void displayAllTasks() {
		System.out.println("All Tasks:");
		displayAllTasks(root);
	}

	private void displayAllTasks(Task root) {
		if (root != null) {
			displayAllTasks(root.left);
			System.out.println("Task Name: " + root.taskName);
			System.out.println("Description: " + root.description);
			System.out.println("Priority: " + root.priority);
			System.out.println("Category: " + root.category);
			System.out.println();
			displayAllTasks(root.right);
		}
	}

	public void displayCompletedTasks() {
		// System.out.println("Completed Tasks:");
		completedTasks.displayCompletedTasks();
	}
}

public class MINI {
	public static void main(String[] args) throws ParseException {
		TaskManager taskManager = new TaskManager();
		Scanner scanner = new Scanner(System.in);

		while (true) {
			System.out.println("Task Manager Menu:");
			System.out.println("1. Add Task");
			System.out.println("2. Display Urgent Tasks");
			System.out.println("3. Display Upcoming Tasks");
			System.out.println("4. Display All Tasks");
			System.out.println("5. Remove Task");
			System.out.println("6. Mark Task as Completed");
			System.out.println("7. Edit Task");
			System.out.println("8. Display Completed Tasks");
			System.out.println("9. Display Tasks by Category");
			System.out.println("0. Exit");

			int choice = scanner.nextInt();
			scanner.nextLine();

			switch (choice) {
				case 1:
					System.out.println("Enter task name:");
					String taskName = scanner.nextLine();
					System.out.println("Enter task description:");
					String description = scanner.nextLine();
					System.out.println("Enter priority (urgent or upcoming):");
					String priority = scanner.nextLine();
					System.out.println("Enter category(work/personal/health):");
					String category = scanner.nextLine();
					System.out.println("Enter due date (MM/dd/yyyy):");
					String dueDateString = scanner.nextLine();

					SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
					Date dueDate = dateFormat.parse(dueDateString);

					taskManager.addTask(taskName, description, priority, category, dueDate);
					break;
				case 2:
					taskManager.displayUrgentTasks();
					break;
				case 3:
					taskManager.displayUpcomingTasks();
					break;
				case 4:
					taskManager.displayAllTasks();
					break;
				case 5:
					System.out.println("Enter task name to remove:");
					String taskNameToRemove = scanner.nextLine();
					taskManager.removeTask(taskNameToRemove);
					break;
				case 6:
					System.out.println("Enter task name to mark as completed:");
					String taskNameToComplete = scanner.nextLine();
					taskManager.markTaskAsCompleted(taskNameToComplete);
					break;
				case 7:
					System.out.println("Enter task name to edit:");
					String taskNameToEdit = scanner.nextLine();
					System.out.println("Enter new task name:");
					String newTaskName = scanner.nextLine();
					System.out.println("Enter new description:");
					String newDescription = scanner.nextLine();

					taskManager.editTask(taskNameToEdit, newTaskName, newDescription);
					break;
				case 8:
					taskManager.displayCompletedTasks();
					break;
				case 9:
					System.out.println("Enter category to display tasks:");
					String categoryToDisplay = scanner.nextLine();
					taskManager.displayTasksByCategory(categoryToDisplay);
					break;
				case 0:
					System.exit(0);
					System.out.println("Task Manager closed!");
					break;
				default:
					System.out.println("Invalid choice.");
					break;
			}
		}
	}
}
/*Task Manager Menu:
1. Add Task
2. Display Urgent Tasks
3. Display Upcoming Tasks
4. Display All Tasks
5. Remove Task
6. Mark Task as Completed
7. Edit Task
8. Display Completed Tasks
9. Display Tasks by Category
0. Exit
1
Enter task name:
ww
Enter task description:
sdf hjyy
Enter priority (urgent or upcoming):
urgent
Enter category(work/personal/health):
work
Enter due date (MM/dd/yyyy):
12/03/2023
Task Manager Menu:
1. Add Task
2. Display Urgent Tasks
3. Display Upcoming Tasks
4. Display All Tasks
5. Remove Task
6. Mark Task as Completed
7. Edit Task
8. Display Completed Tasks
9. Display Tasks by Category
0. Exit
1
Enter task name:
qq
Enter task description:
as ijfesr
Enter priority (urgent or upcoming):
upcoming
Enter category(work/personal/health):
personal
Enter due date (MM/dd/yyyy):
12/04/2023
Task Manager Menu:
1. Add Task
2. Display Urgent Tasks
3. Display Upcoming Tasks
4. Display All Tasks
5. Remove Task
6. Mark Task as Completed
7. Edit Task
8. Display Completed Tasks
9. Display Tasks by Category
0. Exit
2
Urgent Tasks:
Task Name: ww
Description: sdf hjyy
Priority: urgent
Category: work

Task Manager Menu:
1. Add Task
2. Display Urgent Tasks
3. Display Upcoming Tasks
4. Display All Tasks
5. Remove Task
6. Mark Task as Completed
7. Edit Task
8. Display Completed Tasks
9. Display Tasks by Category
0. Exit
3
Upcoming Tasks:
Task Name: qq
Description: as ijfesr
Priority: upcoming
Category: personal

Task Manager Menu:
1. Add Task
2. Display Urgent Tasks
3. Display Upcoming Tasks
4. Display All Tasks
5. Remove Task
6. Mark Task as Completed
7. Edit Task
8. Display Completed Tasks
9. Display Tasks by Category
0. Exit
4
All Tasks:
Task Name: ww
Description: sdf hjyy
Priority: urgent
Category: work

Task Name: qq
Description: as ijfesr
Priority: upcoming
Category: personal

Task Manager Menu:
1. Add Task
2. Display Urgent Tasks
3. Display Upcoming Tasks
4. Display All Tasks
5. Remove Task
6. Mark Task as Completed
7. Edit Task
8. Display Completed Tasks
9. Display Tasks by Category
0. Exit
5
Enter task name to remove:
qq
Task removed successfully
Task Manager Menu:
1. Add Task
2. Display Urgent Tasks
3. Display Upcoming Tasks
4. Display All Tasks
5. Remove Task
6. Mark Task as Completed
7. Edit Task
8. Display Completed Tasks
9. Display Tasks by Category
0. Exit
6
Enter task name to mark as completed:
ww
Task marked as completed
Task Manager Menu:
1. Add Task
2. Display Urgent Tasks
3. Display Upcoming Tasks
4. Display All Tasks
5. Remove Task
6. Mark Task as Completed
7. Edit Task
8. Display Completed Tasks
9. Display Tasks by Category
0. Exit
7
Enter task name to edit:
ww
Enter new task name:
rr
Enter new description:
biuiu iuwhei
Task Manager Menu:
1. Add Task
2. Display Urgent Tasks
3. Display Upcoming Tasks
4. Display All Tasks
5. Remove Task
6. Mark Task as Completed
7. Edit Task
8. Display Completed Tasks
9. Display Tasks by Category
0. Exit
8
Completed Tasks:
ww
Task Manager Menu:
1. Add Task
2. Display Urgent Tasks
3. Display Upcoming Tasks
4. Display All Tasks
5. Remove Task
6. Mark Task as Completed
7. Edit Task
8. Display Completed Tasks
9. Display Tasks by Category
0. Exit
4
All Tasks:
Task Name: rr
Description: biuiu iuwhei
Priority: urgent
Category: work

Task Name: qq
Description: as ijfesr
Priority: upcoming
Category: personal

Task Manager Menu:
1. Add Task
2. Display Urgent Tasks
3. Display Upcoming Tasks
4. Display All Tasks
5. Remove Task
6. Mark Task as Completed
7. Edit Task
8. Display Completed Tasks
9. Display Tasks by Category
0. Exit
9
Enter category to display tasks:
work
work Tasks:
ww
Task Manager Menu:
1. Add Task
2. Display Urgent Tasks
3. Display Upcoming Tasks
4. Display All Tasks
5. Remove Task
6. Mark Task as Completed
7. Edit Task
8. Display Completed Tasks
9. Display Tasks by Category
0. Exit
0
*/